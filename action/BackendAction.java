package vm.action;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.upload.FormFile;

import vm.formbean.BackendActionForm;
import vm.model.Goods;
import vm.service.BackendService;
import vm.vo.SalesReport;


public class BackendAction extends DispatchAction {

	private BackendService backendService = BackendService.getInstance();

	public ActionForward queryGoods(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		Set<Goods> goods = backendService.queryGoods();
		System.out.println("queryGoods...");
		goods.stream().forEach(a -> System.out.println(a));

		request.setAttribute("goodsList", goods);

		return mapping.findForward("goodsListView");
	}

	public ActionForward updateGoods(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		System.out.println("updateGoods...");

		BackendActionForm action = (BackendActionForm) form;
		
		Goods goods = new Goods();
		goods.setGoodsPrice(action.getGoodsPrice());
	    goods.setGoodsQuantity(action.getGoodsQuantity());
	    goods.setStatus(action.getStatus());
		
		if(action.getGoodsID() != null && !action.getGoodsID().trim().isEmpty()){
			goods.setGoodsID(new BigDecimal(action.getGoodsID().trim()));
		}else {
            System.out.println("Goods ID is null or empty");
          
        }

		System.out.println("GoodsID: " + goods.getGoodsID());
		System.out.println("GoodsPrice: " + goods.getGoodsPrice());
		System.out.println("GoodsQuantity: " + goods.getGoodsQuantity());
		System.out.println("Status: " + goods.getStatus());

		boolean updateResult = backendService.updateGoods(goods);

		String message = updateResult ? "商品維護作業成功！" : "商品維護作業失敗！";
		System.out.println(message);

		return mapping.findForward("goodsReplenishmentView");
	}

	public ActionForward goodsReplenishmentView(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		Set<Goods> goods = backendService.queryGoods();
		request.setAttribute("goods", goods);
		

		return mapping.findForward("goodsReplenishment");
	}

	public ActionForward addGoods(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		System.out.println("addGoods...");

		BackendActionForm action = (BackendActionForm) form;
		Goods goods = new Goods();
		goods.setGoodsName(action.getGoodsName());
		goods.setGoodsPrice(action.getGoodsPrice());
	    goods.setGoodsQuantity(action.getGoodsQuantity());
	    goods.setStatus(action.getStatus());
	    
	    if(action.getGoodsID() != null && !action.getGoodsID().trim().isEmpty()){
			goods.setGoodsID(new BigDecimal(action.getGoodsID().trim()));
		}else {
            System.out.println("Goods ID is null or empty");
            goods.setGoodsID(new BigDecimal(0));
        }	    

		String path = "/apache-tomcat-8.0.53/webapps/DrinksImage";
		File uploadDir = new File(path);
		if (!uploadDir.exists()) {
			uploadDir.mkdirs();
		}

		FormFile goodsImage = action.getGoodsImage();
		if (goodsImage != null && goodsImage.getFileSize() > 0) {
			String name = goodsImage.getFileName();
			File file = new File(path, name);

			try (InputStream inps = goodsImage.getInputStream();
					OutputStream oups = new FileOutputStream(file)) {
				byte[] buffer = new byte[1024];
				int bytesRead;
				while ((bytesRead = inps.read(buffer)) != -1) {
					oups.write(buffer, 0, bytesRead);
				}
				goods.setGoodsImageName(name);
			} catch (Exception e) {
				e.printStackTrace();
				return mapping.findForward("failure");
			}
		} else {
			goods.setGoodsImageName("default.jpg"); // 設置默認圖片名
		}

		boolean createResult = backendService.addGoods(goods);

		String message = createResult ? "商品新增上架成功！" : "商品新增上架失敗！";
		System.out.println(message);

		return mapping.findForward("goodsCreateView");
	}

	public ActionForward goodsCreateView(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		Set<Goods> goods = backendService.queryGoods();
		request.setAttribute("goods", goods);
		
		return mapping.findForward("goodsCreate");
	}

	public ActionForward querySalesReport(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		
		String startDate = request.getParameter("queryStartDate");
	    String endDate = request.getParameter("queryEndDate");
	    
		
		List<SalesReport> reports = backendService.querySalesReport(startDate, endDate);
		System.out.println("querySalesReport...");
		reports.stream().forEach(a -> System.out.println(a));
		
		request.setAttribute("reports", reports);
		request.setAttribute("startDate", startDate);
		request.setAttribute("endDate", endDate);

		return mapping.findForward("goodsSaleReportView");
	}

	public ActionForward goodsSaleReportView(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		String startDate = (String)request.getParameter("startDate");
		String endDate = (String)request.getParameter("endDate");
			
	    if (startDate != null && endDate != null) {
            List<SalesReport> reports = backendService.querySalesReport(startDate, endDate);
            System.out.println("goodsSaleReportView...");
            reports.stream().forEach(report -> System.out.println(report));
            request.setAttribute("reports", reports);
        }
		
		return mapping.findForward("goodsSaleReport");
	}

}
