package vm.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import vm.formbean.ShoppingCartGoodsForm;
import vm.model.Goods;
import vm.service.FrontendService;
import vm.vo.ShoppingCartGoods;

public class FrontendAction extends DispatchAction {

	private FrontendService frontendService = FrontendService.getInstance();
	//商品列表
	public ActionForward searchGoods(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		System.out.println("searchGoods method called");
		
		//取得請求參數
		String searchKeyword = request.getParameter("searchKeyword");
		
		String pageNoParam  = request.getParameter("pageNo");
		int pageNo = 1; 
		if (pageNoParam != null && !pageNoParam.isEmpty()) {
	        try {
	            pageNo = Integer.parseInt(pageNoParam);
	        } catch (NumberFormatException e) {
	            // 如果转换失败，可以记录日志并使用默认值
	            System.out.println("Invalid page number format, using default: " + e.getMessage());
	        }
	    }
		
		int pageSize = 6;
		
	    
	    //根據關鍵字或畫面取得商品列表
		List<Goods> goodsList = null;
		if (searchKeyword != null && !searchKeyword.isEmpty()) {
		    System.out.println("Search keyword: " + searchKeyword);
		    goodsList = frontendService.searchGoods(searchKeyword, pageNo, pageSize);
		    System.out.println("Goods list size after search: " + (goodsList != null ? goodsList.size() : "null"));
		} else {
		    goodsList = frontendService.getGoodsList(pageNo, pageSize);
		    System.out.println("Goods list size for all goods: " + (goodsList != null ? goodsList.size() : "null"));
		}
		
		//防止空值異常
		if (goodsList == null) {
		    goodsList = new ArrayList<>();
		}
		System.out.println("Goods list size: " + goodsList.size());
		
		
		int totalRecords;
	    if (searchKeyword != null && !searchKeyword.isEmpty()) {
	        totalRecords = frontendService.getGoodsCount(searchKeyword);
	    } else {
	        totalRecords = frontendService.getGoodsCount(null);  // 全查
	    }
	    
	    
		//分頁計算
		int totalPages = (int) Math.ceil((double) totalRecords / pageSize);
	    int previousPage = pageNo > 1 ? pageNo - 1 : 1;
	    int nextPage = pageNo < totalPages ? pageNo + 1 : totalPages;
	    

	    //回傳至前端
		request.setAttribute("goodsList", goodsList);
		request.setAttribute("pageNo", pageNo);
	    request.setAttribute("totalPages", totalPages);
	    request.setAttribute("searchKeyword", searchKeyword);
	    
	    System.out.println("Page Number: " + pageNo);
	    System.out.println("Search Keyword: " + searchKeyword);
	    
	    
	    System.out.println("searchGoods...");
	    if (goodsList == null || goodsList.isEmpty()) {
	        System.out.println("No goods found.");
	    } else {
	        goodsList.stream().forEach(g -> System.out.println(g));
	    }
	    System.out.println("Goods List Size: " + (goodsList != null ? goodsList.size() : "null"));
	    
		return mapping.findForward("vendingMachineView");
	}
	
	public ActionForward showGoods(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)throws Exception {

            				

		return searchGoods(mapping, form, request, response);
	}

	// 商品加入購物車
	public ActionForward addCartGoods(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		
		ShoppingCartGoodsForm orderForm = (ShoppingCartGoodsForm) form;
		System.out.println(orderForm);
		
		HttpSession session = request.getSession();
		Map<Goods, Integer> cartGoods = (Map<Goods, Integer>) session.getAttribute("cartGoods");
		if(cartGoods != null){
			//有購物車
			Goods goods = createGoods(orderForm.getGoodsID());
			//判斷要加入的商品是否已存在於購物車內
			if(cartGoods.containsKey(goods)){
				//加入的商品已存在於購物車
				//q:購物車商品原數量
				cartGoods.computeIfPresent(goods, (g, q) -> q + orderForm.getBuyQuantity());
			}else{
				//加入的商品不存在於購物車
				goods = frontendService.queryIdByGoods(orderForm.getGoodsID());
				cartGoods.put(goods, orderForm.getBuyQuantity());
			}
		} else{
			//沒有購物車
			cartGoods = new LinkedHashMap<>();
			Goods goods = frontendService.queryIdByGoods(orderForm.getGoodsID());
			cartGoods.put(goods, orderForm.getBuyQuantity());
			session.setAttribute("cartGoods", cartGoods);
		}
		
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		
		JSONObject jsonResponse = new JSONObject();
		jsonResponse.put("success", true);
		List<ShoppingCartGoods> shoppingCartGoods = new ArrayList<>();
		BiConsumer<Goods, Integer> cartAction = (g, q)->{
			ShoppingCartGoods goods = new ShoppingCartGoods();
			goods.setGoodsID(g.getGoodsID());
			goods.setGoodsName(g.getGoodsName());
			goods.setGoodsPrice(g.getGoodsPrice());
			goods.setBuyQuantity(q);
			shoppingCartGoods.add(goods);
		};
		cartGoods.forEach(cartAction);		
		jsonResponse.put("cartItems", JSONArray.fromObject(shoppingCartGoods));
		out.println(jsonResponse.toString());
		out.flush();
		out.close();

		return mapping.findForward("vendingMachineView");
	}
	private Goods createGoods(Long goodsID) {
	    // 根據現有的 Goods 類別創建 Goods 物件的工廠方法
	    Goods goods = new Goods();
	    goods.setGoodsID(goodsID);
	    System.out.println("Created Goods with ID: " + goodsID);
	    return goods;
	}

	
	// 查詢購物車商品請求導結帳(查詢資料庫最新商品價格)
	public ActionForward queryCartGoods(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		
		HttpSession session = request.getSession();
        Map<Goods, Integer> cartGoods = (Map<Goods, Integer>) session.getAttribute("cartGoods");
        if (cartGoods != null) {
            // 更新購物車的商品價格
            cartGoods.forEach((goods, quantity) -> {
                Goods updateGoods = frontendService.queryIdByGoods(goods.getGoodsID());
                goods.setGoodsPrice(updateGoods.getGoodsPrice());
                goods.setGoodsName(updateGoods.getGoodsName()); // 也可以更新其他必要資訊
            });
        }

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        List<ShoppingCartGoods> shoppingCartGoods = new ArrayList<>();
        if (cartGoods != null) {
            cartGoods.forEach((goods, quantity) -> {
                ShoppingCartGoods item = new ShoppingCartGoods();
                item.setGoodsID(goods.getGoodsID());
                item.setGoodsName(goods.getGoodsName());
                item.setGoodsPrice(goods.getGoodsPrice());
                item.setBuyQuantity(quantity);
                shoppingCartGoods.add(item);
            });
        }

        out.println(JSONArray.fromObject(shoppingCartGoods));
        out.flush();
        out.close();

		return mapping.findForward("vendingMachineView");
	}
	//購物車清除
	public ActionForward clearCartGoods(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		
		HttpSession session = request.getSession();
		session.removeAttribute("cartGoods"); 

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.println("{\"message\":\"購物車已清空\"}");
        out.flush();
        out.close();

        return mapping.findForward("vendingMachineView");
    }
	//結帳
	public ActionForward checkout(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws IOException {
		
        HttpSession session = request.getSession();
        Map<Goods, Integer> cartGoods = (Map<Goods, Integer>) session.getAttribute("cartGoods");
        String customerID = request.getParameter("customerID");
        int inputMoney = Integer.parseInt(request.getParameter("inputMoney"));
        
        System.out.println("Received customerID: " + customerID);
        System.out.println("Received inputMoney: " + inputMoney);
        
        int totalPrice = 0;
        boolean purchaseSuccess = false;
        String message = "購物車為空";
        int changeAmount = 0;
        
        if (cartGoods != null) {
            for (Map.Entry<Goods, Integer> entry : cartGoods.entrySet()) {
                Goods goods = entry.getKey();
                Integer quantity = entry.getValue();
                totalPrice += goods.getGoodsPrice() * quantity;
            }
        
         // 檢查用戶是否有足夠的錢購買
            if (inputMoney >= totalPrice) {
                purchaseSuccess = frontendService.updateGoodsQuantity(cartGoods, customerID, inputMoney);
                if (purchaseSuccess) {
                    changeAmount = inputMoney - totalPrice;
                    message = "結帳成功";
                } else {
                    message = "結帳失敗";
                }
            } else {
                message = "金額不足";
                changeAmount = inputMoney;
            }

        // 構建結帳結果
        String jsonResponse = String.format("{\"message\":\"%s\", \"totalPrice\":%d, \"changeAmount\":%d, \"inputMoney\":%d}", message, totalPrice, changeAmount, inputMoney);
        System.out.println("JSON Response: " + jsonResponse);

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.println(jsonResponse);
        out.flush();
        out.close();
        
        // 清空購物車
        session.removeAttribute("cartGoods");
        System.out.println("Cart goods removed from session after checkout");
      } else{
    	  response.setContentType("application/json");
          PrintWriter out = response.getWriter();
          out.println("{\"message\":\"購物車為空\"}");
          out.flush();
          out.close();
      }
        
        
		return null;
	}

}