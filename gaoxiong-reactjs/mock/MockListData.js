export default {
    'get /ds/list': (req, res) => {
        res.json({
            data: [1, 2, 3, 4],
            maxNum: 4
        });
    },
    'get /ds/user/list': (req, res) => {
        res.json(
            [{
                key:"1",
                name:"zhangsan",
                age:32,
                address:'shanghai',
                tags:['程序员','帅气']
            },{
                key:"2",
                name:"2zhangsan",
                age:32,
                address:'shanghai',
                tags:['程序员','帅气']
            },{
                key:"3",
                name:"3zhangsan",
                age:32,
                address:'shanghai',
                tags:['程序员','帅气']
            },{
                key:"4",
                name:"5zhangsan",
                age:32,
                address:'shanghai',
                tags:['程序员','帅气']
            },{
                key:"5",
                name:"5zhangsan",
                age:32,
                address:'shanghai',
                tags:['程序员','帅气']
            }]
        );
    }
}