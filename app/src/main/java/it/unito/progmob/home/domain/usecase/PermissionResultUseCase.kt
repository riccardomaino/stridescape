package it.unito.progmob.home.domain.usecase

class PermissionResultUseCase {
    /**
     * It adds the permission to the queue of permissions if it is not granted and it is not already
     * present in the queue. This function can be invoked using the use
     * case instance itself due to the overload operator `invoke`.
     * @param visiblePermissionDialogQueue The permission queue containing the refused permissions
     * @param permission The string name representing the permission
     * @param isGranted A boolean value used to evaluate if the permission was granted or not
     */
    operator fun invoke(
        visiblePermissionDialogQueue: MutableList<String>,
        permission: String,
        isGranted: Boolean
    ): MutableList<String> {
        if(!isGranted && !visiblePermissionDialogQueue.contains(permission)) {
            visiblePermissionDialogQueue.add(permission)
        }
        return visiblePermissionDialogQueue
    }
}