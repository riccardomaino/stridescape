package it.unito.progmob.home.domain.usecase

import androidx.compose.runtime.snapshots.SnapshotStateList

class DismissPermissionDialogUseCase {
    /**
     * It pops the first entry of the queue of permissions. This function can be invoked using the use
     * case instance itself due to the overload operator `invoke`.
     * @param visiblePermissionDialogQueue The permission queue containing the refused permissions
     */
    operator fun invoke(
        visiblePermissionDialogQueue: SnapshotStateList<String>
    ){
        visiblePermissionDialogQueue.removeFirst()
    }
}