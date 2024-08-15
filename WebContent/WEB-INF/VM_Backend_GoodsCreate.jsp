<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<!DOCTYPE html>
<html lang="zh-Hant">
<head>
    <meta http-equiv="Content-Language" content="zh-tw">
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">   
    <title>販賣機-後臺</title>
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <style>
        h2 {
            color: blue !important;
            padding-left: 200px;
        }
        .goods-create p {
            color: orange !important;
            margin-left: 50px;
            font-size: 2rem;
        }
        .form-group {
            display: flex;
            align-items: center;
            
        }
        .custom-file-label {
            font-size: 1.5rem;
            margin-left: 20px;
            padding: 10px 20px;
            cursor: pointer;
        }
        
        
    </style>    
</head>
<body>
    <%@ include file="VM_FunMenu.jsp" %>
    <br/><br/><HR>
    <h2>商品新增上架</h2>
    <div class="goods-create">
        <form name="addGoodsForm" action="BackendAction.do?action=addGoods" enctype="multipart/form-data" method="post">
            <p>飲料名稱：<input type="text" name="goodsName" size="10" required></p>
            <p>設定價格：<input type="number" name="goodsPrice" size="5" value="0" min="0" max="1000" required></p>
            <p>初始數量：<input type="number" name="goodsQuantity" size="5" value="0" min="0" max="1000" required></p>
            <div class="form-group">
                <p style="margin-bottom: 0;">商品圖片：</p>
                <div class="custom-file" style="width: auto;">
                    <input type="file" name="goodsImage" id="goodsImage" class="custom-file-input" required>
                    <label for="goodsImage" class="custom-file-label">選擇檔案</label>
                </div>
            </div>
			<p>
				<label for="status">商品狀態</label> <select id="status" name="status">
					<option value="" disabled selected>上/下架</option>
					<option value="1">上架</option>
					<option value="0">下架</option>
				</select>
			</p>
			<p style="margin-left:50px;">
				<button type="submit"  class="btn btn-info" style="font-size: 1.5rem; padding: 10px 20px; margin-left:25px; ">送出</button>
            </p>
        </form>
    </div>

    <!-- 顯示錯誤信息 -->
    <c:if test="${not empty status.error}">
        <div style="color: red;">
            <p>Error: ${status.error}</p>
        </div>
    </c:if>

    <script src="js/jquery-3.2.1.min.js"></script>
    <script src="js/popper.min.js"></script>
    <script src="js/bootstrap.min.js"></script>
    
    <script>
        $(document).ready(function(){
            $('#goodsImage').on('change', function(){
                // 取得檔案名稱
                var fileName = $(this).val().split('\\').pop();
                // 更新標籤顯示選擇的檔案名稱
                $(this).next('.custom-file-label').html(fileName);
            });
        });
    </script>
</body>
</html>