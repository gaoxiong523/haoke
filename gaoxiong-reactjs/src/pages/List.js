import React from 'react';
import {connect} from 'dva';

const namespace = 'list';

//说明第一个回调函数,作用将page层和model层进行连接,返回model中的 数据,
//并且,将返回的数据,绑定到this.props

//接受第二个函数,这个函数的作用:将定义的函数绑定到this.props中,调用model层中定义的函数
@connect((state) => {
    return {
        dataList:state[namespace].data,
        maxNum:state[namespace].maxNum
    }
},(dispatch)=>{ //dispatch的作用, 可以调用model层定义的函数
    return{ //将返回的函数绑定到 this.props中
        add: function () {
            dispatch({ //通过dispatch调用 model中定义的函数,通过type属性,指定函数名,格式: namespace / 函数名
                type: namespace +"/addNewData",
            });
        },
        init : ()=>{
            dispatch({
                type: namespace + "/initData"
            });
        }


    }
})
class List extends React.Component {

    constructor(props) {
        super(props);
        // this.state  = {
        //     dataList:[1,2,3],
        //     maxNum:3
        // }
    }


    componentDidMount() {
        //初始化操作,
        this.props.init();
    }

    render() {
        return (
            <div>
                <ul>
                    {
                        this.props.dataList.map((key,value) => {
                            return <li key={key}>{value}</li>
                        })
                    }
                </ul>
                <button onClick={() => {
                    // let maxnum = this.state.maxNum + 1;
                    // let newArr = [...this.state.dataList, maxnum];
                    // this.setState(
                    //     {
                    //         dataList: newArr,
                    //         maxNum: maxnum
                    //     }
                    // );
                    this.props.add();
                }}>点我增加
                </button>
                <button onClick={()=>{
                    // this.props.jian();
                }}>点我减少</button>
            </div>
        );
    }
}

export default List;