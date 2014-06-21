package grailsappdirect

class Result {

    boolean success
    String errorCode
    String message
    String accountIdentifier

    /*The accountIdentifier is managed for the application and pass it in the response XML to AppDirect*/

    String toString() {
        StringBuilder resultMessage = new StringBuilder()
        resultMessage.append("*Result*")
        resultMessage.append("\n")
        final String SUCCESS = "SUCCESS"
        final String FAILURE = "FAILURE"

        resultMessage.append("\n")
        resultMessage.append("  -message $message")
        resultMessage.append("\n")
        if (accountIdentifier != null) {
            resultMessage.append("  -accountIdentifier: $accountIdentifier")
        }
        resultMessage.append("\n")
        resultMessage.append("  -result: ")
        if (success) {
            resultMessage.append(SUCCESS)
        } else {
            resultMessage.append(FAILURE)
        }
        resultMessage.toString()
    }

}
