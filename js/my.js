// function load_home() {
// 	document.getElementById("viewDiv").innerHTML =
// 		'<object type="text/html" data="new.html" width="100%" height="100%"></object>';
// }

var ip = "/index";

function loadXMLDoc()
{
    var xmlhttp;
    if (window.XMLHttpRequest)
    {
        // IE7+, Firefox, Chrome, Opera, Safari 浏览器执行代码
        xmlhttp=new XMLHttpRequest();
    }
    else
    {
        // IE6, IE5 浏览器执行代码
        xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
    }
    xmlhttp.onreadystatechange=function()
    {
        if (xmlhttp.readyState==4 && xmlhttp.status==200)
        {
            document.getElementById("main").innerHTML=xmlhttp.responseText;
        }
    }
    xmlhttp.open("GET",ip+"?query=parkingManagement",true);
    xmlhttp.send();
    alert(xmlhttp.responseText);
}

function StandardPost(html) {
    localStorage.removeItem('callbackHTML');
    localStorage.setItem('callbackHTML',html);
    window.location.href = window.location.href.split('/h5/')[0] + '/h5/callBack.html';
}

function foldsideNav(thisid) { //折叠侧边栏点击响应
	openNav();
	var ele = document.getElementById(thisid);
	ele.setAttribute("open", "open");
	if(thisid=='kongzhitai'){
		ele.style.color="#416aff";
	}else{
		ele.firstElementChild.style.color="#416aff";	
	}
}

function showMsg() {
	// to do
	alert('当前无新消息');
}

function showCompliant() {
	// to do
	alert('当前无投诉');
}

function openDialog() {
	<!--打开登录弹窗-->
	document.getElementById('light').style.display = 'block';
	document.getElementById('fade').style.display = 'block';
}

function closeDialog() {
	<!--关闭登录弹窗-->
	document.getElementById('light').style.display = 'none';
	document.getElementById('fade').style.display = 'none';
}

function logIn() {
	<!--登录-->
	if (document.getElementById('账号').value == '') {
		alert('请输入账号');
	} else if (document.getElementById('密码').value == '') {
		alert('请输入密码');
	} else if (document.getElementById('账号').value.length < 8) {
		alert('账号长度过短');
	} else if (document.getElementById('密码').value.length < 8) {
		alert('密码长度过短');
	} else {
		//to do
	}
}

function reloadDetailsColor(){
	document.getElementById("kongzhitai").style.color="#ffffff";
	document.getElementById("fangchan").firstElementChild.style.color="#ffffff";
	document.getElementById("yezhu").firstElementChild.style.color="#ffffff";
	document.getElementById("tingche").firstElementChild.style.color="#ffffff";
	document.getElementById("shoufei").firstElementChild.style.color="#ffffff";
	document.getElementById("guanliyuan").firstElementChild.style.color="#ffffff";
}

function reloadDetailsOpen(){
	document.getElementById("fangchan").removeAttribute("open");
	document.getElementById("yezhu").removeAttribute("open");
	document.getElementById("tingche").removeAttribute("open");
	document.getElementById("shoufei").removeAttribute("open");
	document.getElementById("guanliyuan").removeAttribute("open");
}

function reloadDetails(){
	reloadDetailsColor();
	reloadDetailsOpen();
}

function setDetails(thisid){
	reloadDetailsColor();
	document.getElementById(thisid).firstElementChild.style.color="#416aff";	
}

function openNav() {
	document.getElementById("mySidenav").style.left = "0px";
	document.getElementById("top").style.left = "0px";
	document.getElementById("foldside").style.left = "-40px";
	document.getElementById("main").style.left = "200px";
}

function closeNav() {
	document.getElementById("mySidenav").style.left = "-200px";
	document.getElementById("top").style.left = "-200px";
	document.getElementById("foldside").style.left = "0px";
	document.getElementById("main").style.left = "40px";
	reloadDetails();
}

function toggleNav() { //侧栏菜单按钮响应
	var l = document.getElementById("mySidenav").style.left;
	if (l == "0px") {
		closeNav();
	} else {
		openNav();
	}
}

// function setMinutes() {
// 	var m = new Date().getMinutes();
// 	if (m < 10) {
// 		m = "0" + m;
// 	}
// 	return m;
// }

// function showTime() {
// 	var showtime = new Date().getFullYear() + "-" + (new Date().getMonth() + 1) + "-" + new Date().getDate() + " " +
// 		new Date().getHours() + ":" + setMinutes() + ":" + new Date().getSeconds();
// 	switch (new Date().getDay()) {
// 		case 1:
// 			showtime += " 星期一";
// 			break;
// 		case 2:
// 			showtime += " 星期二";
// 			break;
// 		case 3:
// 			showtime += " 星期三";
// 			break;
// 		case 4:
// 			showtime += " 星期四";
// 			break;
// 		case 5:
// 			showtime += " 星期五";
// 			break;
// 		case 6:
// 			showtime += " 星期六";
// 			break;
// 		case 7:
// 			showtime += " 星期日";
// 	}
// 	var ele = document.getElementById("time");
// 	ele.innerHTML = showtime;
// }
// window.setInterval("showTime()", 1000);
