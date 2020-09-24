var Main = {
	data() {
		return { //默认返回
			editableTabsValue: '',
			editableTabs: [],
			tabIndex: '',
		}
	},
	methods: {
		addTab(targetName) {
			var info = $.cookie('LoginInfo');
			if (!info) {
				alert('请先登录！');
				return;
			}

			var loginInfo = JSON.parse(info);
			var account = loginInfo.account;
			var token = loginInfo.token;

			let flag = true;
			let tabs = this.editableTabs;
			tabs.forEach((tab, index) => {
				if (tab.title === targetName) {
					flag = false;
					this.editableTabsValue = tab.name;
				}
			});
			if (flag === true) {
				let newTabName = ++this.tabIndex + '';
				this.editableTabs.push({
					title: targetName, //标签页标题
					name: newTabName,
					content: '/index?query=' + targetName + '&account=' + account + '&token=' + token,
				});
				this.editableTabsValue = newTabName;
			}
		},
		removeTab(targetName) {
			let tabs = this.editableTabs;
			let activeName = this.editableTabsValue;
			if (activeName === targetName) {
				tabs.forEach((tab, index) => {
					if (tab.name === targetName) { //选择关闭后新的标签页
						let nextTab = tabs[index + 1] || tabs[index - 1];
						if (nextTab) {
							activeName = nextTab.name;
						}
					}
				});
			}

			this.editableTabsValue = activeName;
			this.editableTabs = tabs.filter(tab => tab.name !== targetName);
		}
	}
}
var Ctor = Vue.extend(Main)
new Ctor().$mount('#app')
