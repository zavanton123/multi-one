package ru.zavanton.accounts_impl.data

import ru.zavanton.accounts_api.data.IAccountRepository
import javax.inject.Inject

class AccountRepository @Inject constructor() : IAccountRepository {

    override fun fetchAccountInfo(id: Long): String {
        return "account info for id $id"
    }
}
