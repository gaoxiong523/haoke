import router from 'umi/router';

import React from 'react';
class HelloWorld extends React.Component{

    render() {
        return (
            <div>
                <h1>hello world</h1>
                <h1>我的{this.props.name}第一个{this.props.children}react组件</h1>
                <button onClick={()=>router.goBack()}>go back</button>
            </div>
        );
    }
}
export default HelloWorld;