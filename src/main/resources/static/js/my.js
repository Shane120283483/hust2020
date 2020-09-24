function getCurTime() {
    var date = new Date();
    var year = date.getFullYear();
    var month =
        date.getMonth() + 1 < 10 ?
            "0" + (date.getMonth() + 1) :
            date.getMonth() + 1;
    var day =
        date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
    var hours =
        date.getHours() < 10 ? "0" + date.getHours() : date.getHours();
    var minutes =
        date.getMinutes() < 10 ?
            "0" + date.getMinutes() :
            date.getMinutes();
    var seconds =
        date.getSeconds() < 10 ?
            "0" + date.getSeconds() :
            date.getSeconds();
    let time =
        year +
        "-" +
        month +
        "-" +
        day +
        " " +
        hours +
        ":" +
        minutes +
        ":" +
        seconds;
    return time
}

function StandardPost(html) {
    localStorage.removeItem('callbackHTML');
    localStorage.setItem('callbackHTML', html);
    window.location.href = window.location.href.split('/h5/')[0] + '/h5/callBack.html';
}

function foldsideNav(thisid) { //折叠侧边栏点击响应
    openNav();
    let ele = document.getElementById(thisid);
    ele.setAttribute("open", "open");
    ele.firstElementChild.style.color = "#416aff";
}

function openDialog() {
    //<!--打开登录弹窗-->
    document.getElementById('light').style.display = 'block';
    document.getElementById('fade').style.display = 'block';
}

function closeDialog() {
    //<!--关闭登录弹窗-->
    document.getElementById('light').style.display = 'none';
    document.getElementById('fade').style.display = 'none';
}

function winClose() {
    window.opener = null;
    window.open("", "_self");
    window.close();
}

function logIn() {
    //<!--登录-->
    if (document.getElementById('账号').value === '') {
        alert('请输入账号');
    } else if (document.getElementById('密码').value === '') {
        alert('请输入密码');
    } else {
        var user_name = document.getElementById("账号").value;
        var password = hex_md5(document.getElementById("密码").value);
        $.post({
            contentType: "application/json;charset=UTF-8",
            url: "/login",
            data: JSON.stringify({ account: user_name, password: password }),
            success: function (ret) {
                var data = JSON.parse(ret);

                var ret = data.return_val;
                if (ret !== 0) {
                    alert(data.token);
                } else {
                    $.cookie("LoginInfo", JSON.stringify(data), { expires: 2 });
                    location.reload();
                }

            },
            error: function () {
                alert('登录失败，请稍后再试');
            }
        });
    }
}

function logout() {
    var info = $.cookie('LoginInfo');
    if (!info) return;

    var account = JSON.parse(info).account;
    $.removeCookie('LoginInfo');
    location.reload();

    $.post({
        contentType: "application/json;charset=UTF-8",
        url: "/logout",
        data: account
    });
}

function checkLogin() {
    var info = $.cookie("LoginInfo");
    if (info !== undefined) {//已登录
        var login_info = JSON.parse(info);
        document.getElementById('user_icon').src = login_info.icon_url;
        document.getElementById('login_button').style.visibility = 'hidden';
        document.getElementById('user_name').style.visibility = 'visible';
        document.getElementById('user_name').textContent = login_info.user_name;
        document.getElementById('logout_button').style.visibility = 'visible';
    } else {
        document.getElementById('user_icon').src = 'img/0.svg';
        document.getElementById('user_name').style.visibility = 'hidden';
        document.getElementById('logout_button').style.visibility = 'hidden';
        document.getElementById('login_button').style.visibility = 'visible';
    }
}

function reloadDetailsColor() {
    document.getElementById("fangchan").firstElementChild.style.color = "#ffffff";
    document.getElementById("tingche").firstElementChild.style.color = "#ffffff";
    document.getElementById("shoufei").firstElementChild.style.color = "#ffffff";
    document.getElementById("guanliyuan").firstElementChild.style.color = "#ffffff";
}

function reloadDetailsOpen() {
    document.getElementById("fangchan").removeAttribute("open");
    document.getElementById("tingche").removeAttribute("open");
    document.getElementById("shoufei").removeAttribute("open");
    document.getElementById("guanliyuan").removeAttribute("open");
}

function reloadDetails() {//侧边栏收缩后重置菜单栏状态
    reloadDetailsColor();
    reloadDetailsOpen();
}

function setDetails(thisid) {//高亮选中菜单
    reloadDetailsColor();
    let ele = document.getElementById(thisid);
    ele.firstElementChild.style.color = "#416aff";
}

function openNav() {
    document.getElementById("mySidenav").style.left = "0px";
    document.getElementById("top").style.left = "0px";
    document.getElementById("main").style.left = "200px";
    document.getElementById("foldside").style.left = "-40px";
}

function closeNav() {
    document.getElementById("mySidenav").style.left = "-200px";
    document.getElementById("top").style.left = "-200px";
    document.getElementById("main").style.left = "40px";
    document.getElementById("foldside").style.left = "0px";
    reloadDetails();
}

function toggleNav() { //侧栏菜单按钮响应
    let l = document.getElementById("mySidenav").style.left;
    if (l === "0px") {
        closeNav();
    } else {
        openNav();
    }
}