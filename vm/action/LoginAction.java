package vm.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import vm.dao.BackendDao;
import vm.model.Member;

public class LoginAction extends DispatchAction {
	
	private BackendDao backendDao = BackendDao.getInstance();
	
	public ActionForward login(ActionMapping mapping, ActionForm form, 
            HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		//登入請求
		ActionForward actFwd = null;
     	HttpSession session = request.getSession();
    	String inputID = request.getParameter("identificationNo");
    	String inputPwd = request.getParameter("password");
    	//依使用者所輸入的帳戶名稱取得 Member
    	Member member = backendDao.memberLogin(inputID, inputPwd);
    	String loginMsg = null;
    	if(member != null){
    		//取得帳戶後進行帳號、密碼比對
    		String identificationNo = member.getIdentificationNo();
    		String password = member.getPassword();
    		if(identificationNo.equals(inputID) && password.equals(inputPwd)){
    			loginMsg = member.getCustomerName() + " 先生/小姐您好!";
    			// 將account存入session scope 以供LoginCheckFilter之後使用!
    			session.setAttribute("member", member);
    			actFwd = mapping.findForward("success");
    		}else{
    			//帳號、密碼錯誤,要求重新登入.
    			loginMsg = "帳號或密碼錯誤";
    			actFwd = mapping.findForward("fail");
    			}
    		}else{
    			loginMsg = "無此帳戶名稱,請重新輸入!";        		
    			actFwd = mapping.findForward("fail");
    			}
    	request.setAttribute("loginMsg", loginMsg); 
    	
    	return actFwd;
	}

	public ActionForward logout(ActionMapping mapping, ActionForm form, 
        HttpServletRequest request, HttpServletResponse response) throws Exception {
	// 登出請求
		HttpSession session = request.getSession();
		session.removeAttribute("member");
		request.setAttribute("loginMsg", "謝謝您的光臨!");
	
		return mapping.findForward("fail");
	}
}
