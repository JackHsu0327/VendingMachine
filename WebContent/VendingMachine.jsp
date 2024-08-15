<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="zh-tw">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="Content-Language" content="zh-tw">
    <title>Vending Machine</title>
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <script type="text/javascript">
		function addCartGoods(goodsID, buyQuantityIdx) {
		    console.log("goodsID:", goodsID);
		    var quantityInput = document.getElementsByName("buyQuantity")[buyQuantityIdx];
		    var buyQuantity = quantityInput.value;
		    console.log("buyQuantity:", buyQuantity);

		    var formData = new FormData();
		    formData.append('action', 'addCartGoods');
		    formData.append('goodsID', goodsID);
		    formData.append('buyQuantity', buyQuantity);

		    var request = new XMLHttpRequest();
		    request.open("POST", "addCartGoods.do", true);
		    request.send(formData);

		    request.onreadystatechange = function() {
		        if (this.readyState == 4 && this.status == 200) {
		            var response = request.responseText;
		            try {
		                var responseJson = JSON.parse(response);

		                if (responseJson.success) {
		                    alert("商品已加入購物車");
		                    quantityInput.value = '';  // 清空購買數量的输入框
		                } else {
		                    alert("加入購物車失敗");
		                }
		            } catch (e) {
		                console.error("JSON parse error:", e);
		                alert("伺服器返回數據格式有誤，請稍後再試。");
		            }
		        }
		    };
		}

		function queryCartGoods() {
		    var formData = new FormData();
		    formData.append('action', 'queryCartGoods');

		    var request = new XMLHttpRequest();
		    request.open("POST", "queryCartGoods.do", true);
		    request.send(formData);

		    request.onreadystatechange = function() {
		        if (this.readyState == 4 && this.status == 200) {
		            var response = request.responseText;
		            try {
		                console.log("Response from server: ", response);  // 调试输出服务器返回的数据
		                var responseJson = JSON.parse(response);
		                
		                // 直接传递解析后的数组
		                if (Array.isArray(responseJson) && responseJson.length > 0) {
		                    displayCartGoods(responseJson);
		                } else {
		                    alert('購物車內沒有商品');
		                }
		            } catch (e) {
		                console.error("解析 JSON 时发生错误:", e);
		                alert("解析服务器响应时发生错误。");
		            }
		        }
		    };
		}

		function displayCartGoods(cartItems) {
		    if (cartItems && cartItems.length > 0) {
		        var output = '';
		        for (var i = 0; i < cartItems.length; i++) {
		            var item = cartItems[i];
		            console.log("Item:", item);  // 调试输出每个商品的信息
		            output += '商品編號: ' + item.goodsID + '\n';
		            output += '商品名稱: ' + item.goodsName + '\n';
		            output += '商品價格: ' + item.goodsPrice + '元\n';
		            output += '購買數量: ' + item.buyQuantity + '\n';
		            
		            // 添加分隔线或空行
		            if (i < cartItems.length - 1) { // 不是最后一个商品时添加分隔线
		                output += '-----------------------------------\n';  // 可以替换为其他字符或样式
		            }
		        }
		        alert(output);
		    } else {
		        alert('購物車為空');
		    }
		}

		
		function clearCartGoods(){
			var formData = new FormData();
			formData.append('action', 'clearCartGoods');
			// 送出清空購物車商品請求
			var request = new XMLHttpRequest();
			request.open("POST", "clearCartGoods.do");			
			request.send(formData);			
		}
		
		function submitCheckout() {
		    var customerID = document.getElementsByName("customerID")[0].value;
		    var inputMoneyInput = document.getElementsByName("inputMoney")[0];
		    var inputMoney = inputMoneyInput.value;

		    var formData = new FormData();
		    formData.append('action', 'checkout');
		    formData.append('customerID', customerID);
		    formData.append('inputMoney', inputMoney);

		    var request = new XMLHttpRequest();
		    request.open("POST", "FrontendAction.do", true);
		    request.send(formData);

		    request.onreadystatechange = function() {
		        if (this.readyState == 4 && this.status == 200) {
		            var response = request.responseText;
		            var responseJson = JSON.parse(response);

		            alert("結帳結果: " + responseJson.message + 
		                  "\n投入金額: " + inputMoney + "元" +  // 这里确保使用的是前面传递的 inputMoney
		                  "\n總金額: " + responseJson.totalPrice + "元" +
		                  "\n找零金額: " + responseJson.changeAmount + "元");
		         	// 清空输入的投入金额
		            inputMoneyInput.value = '';
		            		            
		        }
		    }
		}
		</script>
</head>
<body>
    <div class="container">
        <!-- 第一行按钮和搜索框 -->
        <div class="row">
            <div class="col text-right">
                <form class="form-inline d-inline" action="FrontendAction.do" method="get">
                    <input type="hidden" name="action" value="searchGoods"/>
                    <input type="hidden" name="pageNo" value="1"/>
                    <input type="text" name="searchKeyword" class="form-control mr-sm-2" placeholder="Search"/>
                    <button type="submit" class="btn btn-outline-success">商品查詢</button>
                </form>
                <button class="btn btn-outline-primary ml-2" onclick="queryCartGoods()">查看購物車</button>
                <button class="btn btn-outline-secondary ml-2" onclick="clearCartGoods()">清空購物車</button>
            </div>
        </div>
        
        <!-- 第二行主要内容 -->
        <div class="row mt-4">
            <!-- 左侧1/3 -->
            <div class="col-md-4 text-center">
                <img src="DrinksImage/coffee.jpg" alt="Coffee" class="img-fluid">
                <h1>${sessionScope.member.customerName}先生/小姐您好!</h1>
                <p><a href="BackendAction.do?action=queryGoods" class="btn btn-link">後臺頁面</a>
                <a href="LoginAction.do?action=logout" class="btn btn-link">登出</a></p>
                <form onsubmit="submitCheckout(); return false;" class="mt-4">
                    <input type="hidden" name="action" value="checkout"/>
                    <input type="hidden" name="customerID" value="${sessionScope.member.identificationNo}"/>
                    <label for="inputMoney"><b>投入:</b></label>
                    <input type="number" name="inputMoney" id="inputMoney" class="form-control" max="100000" min="0" value="0"/> 元
                    <button type="submit" class="btn btn-outline-primary mt-2">送出</button>
                </form>
                <c:if test="${not empty transaction}">
                    <div class="transaction-summary mt-4">
                        <p>~~~~~~~ 消費明細 ~~~~~~~</p>
                        <p>投入金額：${transaction.inputMoney}</p>
                        <p>購買金額：${transaction.purchaseAmount}</p>
                        <p>找零金額：${transaction.changeAmount}</p>
                        <p>購買商品：${transaction.buyGoods}</p>
                        <ul>
                            <c:forEach var="item" items="${transactionDetails.items}">
                                <li>${item.name} ${item.price} 元 * ${item.quantity}</li>
                            </c:forEach>
                        </ul>
                    </div>
                </c:if>
            </div>
            
            <!-- 右侧2/3 -->
            <div class="col-md-8">
                <form action="FrontendAction.do" method="post">
                    <input type="hidden" name="action" value="buyGoods"/>
                    <div class="row">
                        <c:forEach var="goods" items="${goodsList}" varStatus="status">
                            <div class="col-md-4 text-center product-item mb-4">
                                <h5>${goods.goodsName}</h5>
                                <p>${goods.goodsPrice} 元/罐</p>
                                <img src="DrinksImage/${goods.goodsImageName}" alt="${goods.goodsName}" class="img-fluid">
                                <p>購買<input type="number" name="buyQuantity" min="0" max="${goods.goodsQuantity}" value="0" class="form-control mt-2">罐</p>
                                <button type="button" class="btn btn-outline-primary" onclick="addCartGoods(${goods.goodsID}, ${status.index})">加入購物車</button>
                                <p class="text-danger mt-2">(庫存 ${goods.goodsQuantity} 罐)</p>
                            </div>
                            <c:if test="${status.index % 3 == 2}"></c:if>
                        </c:forEach>
                    </div>
                </form>
            </div>
        </div>
        
        <!-- 分頁 -->
        <div class="row">
            <div class="col text-right">
                <nav aria-label="Page navigation">
                    <ul class="pagination justify-content-end">
                        <c:if test="${pageNo > 1}">
                            <li class="page-item">
                                <a class="page-link" href="FrontendAction.do?action=searchGoods&pageNo=${pageNo-1}&amp;action=searchGoods&amp;searchKeyword=${searchKeyword}">上一页</a>
                            </li>
                        </c:if>
                        <c:forEach var="i" begin="1" end="${totalPages}">
                            <li class="page-item ${pageNo == i ? 'active' : ''}">
                                <a class="page-link" href="FrontendAction.do?pageNo=${i}&amp;action=searchGoods&amp;searchKeyword=${searchKeyword}" 
           					   style="${pageNo == i ? 'color:red;' : ''}">${i}</a>
                            </li>
                        </c:forEach>
                        <c:if test="${pageNo < totalPages}">
                            <li class="page-item">
                                <a class="page-link" href="FrontendAction.do?pageNo=${pageNo + 1}&amp;action=searchGoods&amp;searchKeyword=${searchKeyword}">下一页</a>
                            </li>
                        </c:if>
                    </ul>
                </nav>
            </div>
        </div>
    </div>
    
    <script src="js/jquery-3.2.1.min.js"></script>
    <script src="js/popper.min.js"></script>
    <script src="js/bootstrap.min.js"></script>
</body>
</html>
