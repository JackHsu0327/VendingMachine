package vm.action;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import vm.formbean.BackendActionForm;
import vm.model.Goods;
import vm.service.FrontendService;

public class FrontendAction extends DispatchAction {
	
	private FrontendService frontendService = FrontendService.getInstance();
	
	public ActionForward searchGoods(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception{
	
//		BackendActionForm action = (BackendActionForm) form;
//		Goods good = new Goods();
//		BeanUtils.copyProperties(good, action);
		String searchKeyword = request.getParameter("searchKeyword");
		Set<Goods> goods = frontendService.searchGoods(searchKeyword);
		System.out.println("searchGoods...");
		goods.stream().forEach(g -> System.out.println(g));
		
		
	return mapping.findForward("vendingMachineView");	
	}	
	public ActionForward buyGoods(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		
	return mapping.findForward("");
	}	
	
}