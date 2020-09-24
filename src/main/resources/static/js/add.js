var Main = {
    data() {
        return {
            form: {
                area: '',
                owner: '',
                address: '',
                data:'',

                username: '',
                password: '',
                employDate: '',
                birthday: '',
                createTime: '',
                sex: '',
                phone: '',
                title: '',
                privilege: '',
                electricity: '',
                electricityFee: '',
                eleFee:'',
                water: '',
                waterFee: '',
                gas: '',
                gasFee: '',
                heating: '',
                management: '',
                parking: '',
                currentData: '',
                parkingId:'',
                houseId:'',

                house: '',
                account: '',
                num: '',
                carId: '',

                dataDate: '',

                date: this.getCurTime().substr(0, 10),
                entryDate: this.getCurTime(),
                IdCard: '',
                id: '',
            },
        }
    },
    methods: {
        formatStr(val) {
            this.form.date = val;
        },
        formatEmployDate(val) {
            this.form.employDate = val;
        },
        formatBirthday(val) {
            this.form.birthday = val;
        },
        getCurTime() {
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
        },
        checkInput(regex, src, info) {
            if (regex.test(src)) return true;
            alert(info);
            return false;
        },

        onSubmit(form, moduleName) {
            var info = $.cookie('LoginInfo');
            if (!info) {
                alert('请先登录！');
                return;
            }
            var loginInfo = JSON.parse(info);
            var account = loginInfo.account;
            var token = loginInfo.token;
            
            var idChecker = /^\d*[\dX]$/;
            var floatChecker = /^\d+(\.\d+)?$/;
            var uintChecker = /^\d+$/;

            if (moduleName === '房产管理') {
                if (!this.checkInput(idChecker, form.owner, '身份证号格式错误') 
                 || !this.checkInput(floatChecker, form.area, '面积格式错误' )
                 || !this.checkInput(uintChecker, form.phone, '电话格式错误')) 
                    return;
                var data = { owner: form.owner, area: form.area, address: form.address, phone:form.phone};
            }
            else if(moduleName === '停车信息管理') {
                var data = { carId: form.carId, entryDate: form.entryDate};
            }
            else if(moduleName === '管理员列表')
            {
                if ( !this.checkInput(uintChecker,form.phone, '电话格式错误' ))
                    return;
                var data = {account:form.account,name:form.name,sex:form.sex,phone:form.phone,title:form.title,birthday:form.birthday,employDate:form.employDate};
            }
            else if(moduleName === '车位使用管理'){
                if (!this.checkInput(idChecker, form.owner, '身份证号格式错误')
                    || !this.checkInput(uintChecker, form.phone, '电话格式错误'))
                    return;
                var data = {carId:form.carId,address:form.address,owner:form.owner, phone:form.phone};

            }
            else if(moduleName === '价格管理'){
                if (!this.checkInput(floatChecker, form.electricity, '电表价格格式错误')
                    ||!this.checkInput(floatChecker,form.water, '水表价格格式错误' )
                    ||!this.checkInput(floatChecker,form.gas, '煤气价格格式错误')
                    ||!this.checkInput(floatChecker,form.parking, '停车位价格格式错误'))
                    return;
                var data = {electricity:form.electricity,water:form.water,gas:form.gas,parking:form.parking}
            }
            else if((moduleName === '水费管理')||(moduleName ==='电费管理')||(moduleName==='煤气费管理')){
                if (!this.checkInput(floatChecker, form.currentData, '数据格式错误')
                    ||!this.checkInput(uintChecker, form.houseId, '房屋ID错误'))
                    return;
                var data = {houseId:form.houseId,currentData:form.currentData,date:form.date}
            }

            for (var i in data)
                if (!data[i]) {
                    alert('字段不能为空');
                    return;
                } 

            $.post({
                data: JSON.stringify(data),
                contentType: "application/json;charset=UTF-8",
                url: '/' + moduleName + '/add?account=' + account + '&token=' + token,
                success: function (ret) {
                    var tar = JSON.parse(ret);
                    if (tar.return_val === 0) {
                        parent.location.reload();
                    } else {
                        alert(tar.info);
                    }
                },
                error: function (ret) {
                    alert('请求失败，请稍后再试');
                }
            });
        },
        onRowClick(row, event, column) {
            this.currentRowIndex = row.row_index;
        },

    }

}

var Ctor = Vue.extend(Main)
new Ctor().$mount('#app')