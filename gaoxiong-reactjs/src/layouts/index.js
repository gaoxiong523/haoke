import React from 'react';
import {Layout} from 'antd';
import {Menu, Icon, Button} from 'antd';
import Link from 'umi/link';
const {SubMenu} = Menu;

const {Header, Footer, Sider, Content} = Layout;

class BasicLayout extends React.Component {


    state = {
        collapsed: false,
    };

    toggleCollapsed = () => {
        this.setState({
            collapsed: !this.state.collapsed,
        });
    };


    render() {
        return (
            <div>
                <Layout>
                    <Sider width={256} style={{minHeight: '100vh', color: 'white'}}>
                        <div style={{width: 256}}>
                            <div style={{height: '32px', background: 'rgba(255,255,255,.2)', margin: '16px'}}/>
                            <Button type="primary" onClick={this.toggleCollapsed} style={{marginBottom: 16}}>
                                <Icon type={this.state.collapsed ? 'menu-unfold' : 'menu-fold'}/>
                            </Button>
                            <Menu
                                defaultSelectedKeys={['1']}
                                defaultOpenKeys={['sub1']}
                                mode="inline"
                                theme="dark"
                                inlineCollapsed={this.state.collapsed}
                            >

                                <SubMenu
                                    key="sub1"
                                    title={
                                        <span>
                <Icon type="user"/>
                <span>用户管理</span>
              </span>
                                    }
                                >
                                    <Menu.Item key="1">
                                        <Link to={"/user/UserAdd"}>新增用户</Link>
                                    </Menu.Item>
                                    <Menu.Item key="2">
                                        <Link to={"/user/UserList"}>用户列表</Link>
                                    </Menu.Item>
                                </SubMenu>
                            </Menu>
                        </div>
                    </Sider>
                    <Layout>
                        <Header style={{background: '#fff', textAlign: 'center', padding: 0}}>Header</Header>
                        <Content style={{margin: '24px 16px 0'}}>
                            <div style={{padding: 24, background: '#fff', minHeight: 360}}>
                                {
                                    this.props.children
                                }
                            </div>
                        </Content>
                        <Footer style={{textAlign: 'center'}}>后台 系统 @2019 Created by gaoxing</Footer>
                    </Layout>
                </Layout>
            </div>
        );
    }
}

export default BasicLayout;