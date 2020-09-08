var Main = {
	data() {
		return { //默认返回
			editableTabsValue: '1',
			editableTabs: [{
				title: '我的桌面',
				name: '1',
				content: '我的桌面',
			}],
			tabIndex: 1
		}
	},
	methods: {
		addTab(targetName) {
			let flag = true;
			let tabs = this.editableTabs;
			tabs.forEach((tab, index) => {
				if (tab.title === targetName) {
					flag = false;
					this.editableTabsValue = tab.name;
				}
			});
			if (flag===true) {
				let newTabName = ++this.tabIndex + '';
				this.editableTabs.push({
					title: targetName, //标签页标题
					name: newTabName,
					content: targetName + '', //标签页内容
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
