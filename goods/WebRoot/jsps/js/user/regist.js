$(function(){
	/*
	 * 1.遍历所有的错误信息，循环遍历之。调用一个方法来确定是否显示错误信息！
	 */
	$(".errorclass").each(function(){
		showError($(this));//遍历每个元素，使用每个元素来调用showError方法
	});
	
	/*
	 * 2.切换注册按钮的图片
	 */
	$("#submitBtn").hover(
	    function(){
	    	$("#submitBtn").attr("src","/goods/images/regist2.jpg");
	    },
	    function(){
	    	$("#submitBtn").attr("src","/goods/images/regist1.jpg");
	    }
	 
	);
});

/*
 * 判断当前元素是否存在内容，存在则显示，否则显示！
 */
function showError(ele){
	var text = ele.text();//获取元素内容
	if(!text){//如果没有内容
		ele.css("display","none");//隐藏元素
	}else{//如果有内容
	    ele.css("display","");//显示元素
	}
}

/*
 * 换一张验证码
 */
function _hyz(){
	/*
	 * 1.获取<img>元素
	 * 2.重新设置src
	 * 3.使用毫秒添加参数
	 */
	
	$("#imgVerifyCode").attr("src","/goods/VerifyCodeServlet?a=" + new Date().getTime());
}
