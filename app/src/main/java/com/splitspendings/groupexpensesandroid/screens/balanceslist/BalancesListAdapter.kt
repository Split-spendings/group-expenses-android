package com.splitspendings.groupexpensesandroid.screens.balanceslist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.splitspendings.groupexpensesandroid.databinding.ListItemBalanceBinding
import com.splitspendings.groupexpensesandroid.databinding.ListItemBalanceHeaderWithUserBinding
import com.splitspendings.groupexpensesandroid.model.AppUser
import com.splitspendings.groupexpensesandroid.model.Balance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val ITEM_VIEW_TYPE_HEADER_WITH_USER = 0
private const val ITEM_VIEW_TYPE_BALANCE_ITEM = 1

class BalancesListAdapter(
    private val balanceItemClickListener: BalanceItemClickListener
) : ListAdapter<BalancesListItem, RecyclerView.ViewHolder>(BalanceDiffCallback()) {

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is BalancesListItem.HeaderWithUserItem -> ITEM_VIEW_TYPE_HEADER_WITH_USER
            is BalancesListItem.BalanceItem -> ITEM_VIEW_TYPE_BALANCE_ITEM
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_HEADER_WITH_USER -> HeaderWithUserViewHolder.from(parent)
            ITEM_VIEW_TYPE_BALANCE_ITEM -> BalanceItemViewHolder.from(parent)
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    fun addHeadersAndSubmitList(list: List<Balance>?) {
        adapterScope.launch {
            val items = list?.let {
                groupBalancesByWithAppUser(list)
            } ?: ArrayList()
            withContext(Dispatchers.Main) {
                submitList(items)
            }
        }
    }

    private fun groupBalancesByWithAppUser(list: List<Balance>): List<BalancesListItem> {
        val map = HashMap<AppUser, ArrayList<Balance>>()
        list.forEach {
            map[it.withAppUser]?.add(it) ?: map.put(it.withAppUser, arrayListOf(it))
        }
        val items = ArrayList<BalancesListItem>()
        map.keys.forEach {
            items.add(BalancesListItem.HeaderWithUserItem(it))
            items.addAll(map[it]!!.map { balance -> BalancesListItem.BalanceItem(balance) })
        }
        return items
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HeaderWithUserViewHolder -> {
                val headerWithUser = getItem(position) as BalancesListItem.HeaderWithUserItem
                holder.bind(headerWithUser.appUser)
            }
            is BalanceItemViewHolder -> {
                val balanceItem = getItem(position) as BalancesListItem.BalanceItem
                holder.bind(balanceItem.balance, balanceItemClickListener)
            }
        }
    }

    class HeaderWithUserViewHolder private constructor(val binding: ListItemBalanceHeaderWithUserBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(appUser: AppUser) {
            binding.appUser = appUser
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): HeaderWithUserViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemBalanceHeaderWithUserBinding.inflate(layoutInflater, parent, false)
                return HeaderWithUserViewHolder(binding)
            }
        }
    }

    class BalanceItemViewHolder private constructor(val binding: ListItemBalanceBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(balance: Balance, clickItemClickListener: BalanceItemClickListener) {
            binding.balance = balance
            binding.clickListener = clickItemClickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): BalanceItemViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemBalanceBinding.inflate(layoutInflater, parent, false)
                return BalanceItemViewHolder(binding)
            }
        }
    }
}


class BalanceDiffCallback : DiffUtil.ItemCallback<BalancesListItem>() {

    override fun areItemsTheSame(oldItem: BalancesListItem, newItem: BalancesListItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: BalancesListItem, newItem: BalancesListItem): Boolean {
        return oldItem == newItem
    }
}


class BalanceItemClickListener(val clickListener: (balanceId: Long) -> Unit) {
    fun onClick(balance: Balance) = clickListener(balance.id)
}

sealed class BalancesListItem {

    abstract val id: String

    data class HeaderWithUserItem(val appUser: AppUser) : BalancesListItem() {
        override val id = appUser.id
    }

    data class BalanceItem(val balance: Balance) : BalancesListItem() {
        override val id = balance.id.toString()
    }
}
