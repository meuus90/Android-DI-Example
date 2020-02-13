package com.dependency_injection.sample.viewmodel.item

import androidx.lifecycle.viewModelScope
import com.dependency_injection.base.network.Resource
import com.dependency_injection.base.utility.Parameters
import com.dependency_injection.base.utility.SingleLiveEvent
import com.dependency_injection.sample.usecase.item.ItemListUseCase
import com.dependency_injection.sample.viewmodel.BaseViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ItemListViewModel
@Inject
constructor(private val useCase: ItemListUseCase) : BaseViewModel<Parameters, Int>() {
    internal var assets = SingleLiveEvent<Resource>()

    override fun pullTrigger(params: Parameters) {
        viewModelScope.launch {
            assets = useCase.execute(viewModelScope, params)
        }
    }
}