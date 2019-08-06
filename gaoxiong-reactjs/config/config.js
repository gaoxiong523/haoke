//到处一个对象,暂时设置为 空,后面再添加内容
export default {
    plugins: [ //引入插件
        ['umi-plugin-react', {
            //暂时不引用插件的任何 功能
            dva: true, //开启dva功能
            antd: true //开启 Ant Design功能
        }],
    ],
};