package vm.formbean;

import org.apache.struts.action.ActionForm;

public class LoginActionForm extends ActionForm {

	private String identificationNo;
	
	private String password;
	
	private String customerName;

	public String getIdentificationNo() {
		return identificationNo;
	}

	public void setIdentificationNo(String identificationNo) {
		this.identificationNo = identificationNo;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	@Override
	public String toString() {
		return "Member [identificationNo=" + identificationNo + ", password="
				+ password + ", customerName=" + customerName + "]";
	}
}
