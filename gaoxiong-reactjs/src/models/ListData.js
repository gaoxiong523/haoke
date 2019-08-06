import request from '../util/request';

export default {
    namespace: 'list',
    state: {
        data: [],
        maxNum: 0
    },
    reducers: { //定义的一些函数 ,用于改变model的数据
        addNewData: function (state,result) { //这里state指的是更新之前的状态数据
            if (result.data) { //如果 state中存在data数据,就直接返回,在 做 初始化 的 操作
                return result.data;
            }
            let maxnum = state.maxNum + 1;
            let newArr = [...state.data, maxnum];
            return {
                data: newArr,
                maxNum: maxnum
            }
        }
    },
    effects: {//新增effects配置用于异步加载数据
        * initData(params, sagaEffects) {//定义异步方法
            const {call, put} = sagaEffects;//获取到 call,put方法
            const url = "/ds/list"; //定义 请求的url
            let data = yield call(request, url); //执行请求
            yield put({
                type: "addNewData",//指定调用的 方法 名
                data:data //传递ajax回来的数据
            });

        }
    }
}