import React from 'react';
import {Table, Divider, Tag} from 'antd';
import {connect} from 'dva';

const columns = [
    {
        title: '姓名',//列的标题
        dataIndex: 'name',//绑定的数据属性
        key: 'name',
        render: text => <a href="javascript:;">{text}</a>,
    },
    {
        title: '年龄',
        dataIndex: 'age',
        key: 'age',
    },
    {
        title: '地址',
        dataIndex: 'address',
        key: 'address',
    },
    {
        title: '标签',
        key: 'tags',
        dataIndex: 'tags',
        render: tags => (
            <span>
        {tags.map(tag => {
            let color = tag.length > 5 ? 'geekblue' : 'green';
            if (tag === 'loser') {
                color = 'volcano';
            }
            return (
                <Tag color={color} key={tag}>
                    {tag.toUpperCase()}
                </Tag>
            );
        })}
      </span>
        ),
    },
    {
        title: 'Action',
        key: 'action',
        render: (text, record) => (
            <span>
        <a href="javascript:;">编辑 {record.name}</a>
        <Divider type="vertical"/>
        <a href="javascript:;">删除</a>
      </span>
        ),
    },
];

const namespace = 'userList';

@connect((state) => {
        return {
            data: state[namespace].list
        }
    },
    (dispatch) => {
        return {
            initUserData: () => {
                dispatch({
                    type: namespace + "/initData"
                });
            }
        }
    }
)
class UserList extends React.Component {


    componentDidMount() {
        this.props.initUserData();
    }

    render() {
        return (
            <div>
                <div style={{textAlign: 'center'}}>用户列表</div>
                <Table columns={columns} dataSource={this.props.data} pagination={{position: "bottom", total: 500, pageSize: 10}}/>
            </div>
        )
    }
}

export default UserList;