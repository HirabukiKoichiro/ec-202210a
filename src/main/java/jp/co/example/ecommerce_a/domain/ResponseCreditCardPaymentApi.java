package jp.co.example.ecommerce_a.domain;

/**
 * クレジットカード決済APIのレスポンス情報を扱うドメインクラス.
 * 
 * @author kumagaimayu
 *
 */
public class ResponseCreditCardPaymentApi {

	/** ステータス情報 */
	private String status;
	/** レスポンスメッセージ */
	private String message;
	/** エラーコード */
	private String error_code;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getError_code() {
		return error_code;
	}

	public void setError_code(String error_code) {
		this.error_code = error_code;
	}

	@Override
	public String toString() {
		return "ResponseCreditCardPaymentApi [status=" + status + ", message=" + message + ", error_code=" + error_code
				+ "]";
	}
}
