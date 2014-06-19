package grailsappdirect

/*
*
* Descriptions
*
- USER_ALREADY_EXISTS: This error code is typically used when AppDirect admins try to buy subscriptions for apps they have already purchased directly from the Application Vendor. In this scenario, we'll show users an error message and prompt them to link their accounts.
- USER_NOT_FOUND: This error code is typically used when AppDirect admins try to unassign users not found in the Application Vendor's account.
- ACCOUNT_NOT_FOUND: This error code is typically used when AppDirect admins try to add or remove users from an account not found in the Application Vendor's records.
- MAX_USERS_REACHED: This error code is typically used when AppDirect admins try to assign users beyond the limit of the number of seats available. AppDirect will typically prevent that from happening by monitoring app usage.
- UNAUTHORIZED: This error code is returned when users try any action that is not authorized for that particular application. For example, if an application does not allow the original creator to be unassigned.
- OPERATION_CANCELED: This error code is returned when a user manually interrupts the operation (clicking cancel on the account creation page, etc.).
- CONFIGURATION_ERROR: This error code is returned when the vendor endpoint is not currently configured.
- INVALID_RESPONSE: This error code is returned when the vendor was unable to process the event fetched from AppDirect.
- UNKNOWN_ERROR: This error code may be used when none of the other error codes apply.
*
* */

public enum ErrorCode {
    USER_ALREADY_EXISTS("USER_ALREADY_EXISTS"), USER_NOT_FOUND("USER_NOT_FOUND"),
    ACCOUNT_NOT_FOUND("ACCOUNT_NOT_FOUND"), MAX_USERS_REACHED("MAX_USERS_REACHED"),
    UNAUTHORIZED("UNAUTHORIZED"), OPERATION_CANCELED("OPERATION_CANCELED"),
    CONFIGURATION_ERROR("CONFIGURATION_ERROR"), INVALID_RESPONSE("INVALID_RESPONSE"), UNKNOWN_ERROR("UNKNOWN_ERROR")

    String id

    ErrorCode(String id) {
        this.id = id
    }
}
