function openDialog() {
	<!--打开登录弹窗-->
	document.getElementById('light').style.display = 'block';
	document.getElementById('fade').style.display = 'block'
}

function closeDialog() {
	<!--关闭登陆弹窗-->
	document.getElementById('light').style.display = 'none';
	document.getElementById('fade').style.display = 'none'
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
	}else{
		//to do
	}
}
function openNav() {
	document.getElementById("mySidenav").style.width = "200px";
	document.getElementById("main").style.marginLeft = "200px";
}

function closeNav() {
	document.getElementById("mySidenav").style.width = "0";
	document.getElementById("main").style.marginLeft = "0";
}

function toggleNav() {
	var w1 = document.getElementById("main").style.marginLeft;
	if (w1 == "200px") {
		closeNav();
	} else {
		openNav();
	}
}

function setminutes() {
	var m = new Date().getMinutes();
	if (m < 10) {
		m = "0" + m;
	}
	return m;
}

function showtime() {
	var showtime = new Date().getFullYear() + "-" + (new Date().getMonth() + 1) + "-" + new Date().getDate() + " " +
		new Date().getHours() + ":" + setminutes() + ":" + new Date().getSeconds();
	switch (new Date().getDay()) {
		case 1:
			showtime += " 星期一";
			break;
		case 2:
			showtime += " 星期二";
			break;
		case 3:
			showtime += " 星期三";
			break;
		case 4:
			showtime += " 星期四";
			break;
		case 5:
			showtime += " 星期五";
			break;
		case 6:
			showtime += " 星期六";
			break;
		case 7:
			showtime += " 星期日";
	}
	var ele = document.getElementById("time");
	ele.innerHTML = showtime;
}
window.setInterval("showtime()", 1000);