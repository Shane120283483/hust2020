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