import React from 'react';
import {Icon} from 'semantic-ui-react';

//导入apolloclient
import ApolloClient from 'apollo-boost';
// import gql from "graphql-tag";
import {gql} from "apollo-boost";

const BMap = window.BMap;
const BMapLib = window.BMapLib;
//定义客户端
const client = new ApolloClient({
    uri: "http://127.0.0.1:9002/house/graphql"
});

//定义查询
const GET_MAP_HOUSE = gql`
    query queryMapHouseData($lng: Float, $lat: Float, $zoom: Int) {
        MapHouseData(lng: $lng, lat: $lat, zoom: $zoom) {
            list {
                x
                y
            }
        }
    }
`;

const shouMapMarker = (xys, map) => {
    let markers = [];
    let pt = null;
    for (let i in xys) {
        pt = new BMap.Point(xys[i].x, xys[i].y);
        markers.push(new BMap.Marker(pt));
    }
    // 地图上覆盖物的聚合效果
    let markerClusterer = new BMapLib.MarkerClusterer(map, {
        markers: markers,
        girdSize: 100,
        styles: [{
            background: 'rgba(12,181,106,0.9)',
            size: new BMap.Size(92, 92),
            textSize: '16',
            textColor: '#fff',
            borderRadius: 'true'
        }],
    });
    markerClusterer.setMaxZoom(50);
    markerClusterer.setGridSize(50);
}

class MapHouse extends React.Component {
    constructor(props) {
        super(props);
    }

    componentDidMount() {
        let defaultX = 116.43244;
        let defaultY = 39.929986;
        let defaultZoom = 12;
        // 百度地图API功能

        // 创建Map实例
        let map = new BMap.Map("allmap");
        // 初始化地图,设置中心点坐标和地图级别
        map.centerAndZoom(new BMap.Point(defaultX, defaultY), defaultZoom);
        // 添加地图类型控件
        map.addControl(new BMap.MapTypeControl());
        // 设置地图缩放
        map.addControl(new BMap.ScaleControl({
            anchor: window.BMAP_NAVIGATION_CONTROL_ZOOM
        }));
        // 设置地图导航
        map.addControl(new BMap.NavigationControl({
            enableGeolocation: true
        }));
        // 设置缩略图控件。
        map.addControl(new BMap.OverviewMapControl());
        // 设置地图显示的城市 此项是必须设置的
        map.setCurrentCity("北京");
        // 开启鼠标滚轮缩放
        map.enableScrollWheelZoom(true);
        //添加拖动完成 事件
        let showInfo = () => {
            let cp = map.getCenter();//lng 经度, lat 维度
            let zoom = map.getZoom(); //缩放级别
            console.log(cp.lng + "," + cp.lat + " --> " + zoom);
            //调用api 获取数据,回调添加地图 聚合物
            client.query({
                query: GET_MAP_HOUSE,
                variables: {"lng": cp.lng, "lat": cp.lat, "zoom": zoom}
            }).then(result => {
                let xys = result.data.MapHouseData.list;
                shouMapMarker(xys, map);
            })
        }
        map.addEventListener("dragend", showInfo);
        map.addEventListener("zoomend", showInfo);
        map.addEventListener("zoomstart",()=>{map.clearOverlays();})
        map.addEventListener("dragstart",()=>{map.clearOverlays();})
        // 测试数据
        // var xy = [{
        //   'x': 116.43244,
        //   'y': 39.929986
        // }, {
        //   'x': 116.424355,
        //   'y': 39.92982
        // }, {
        //   'x': 116.423349,
        //   'y': 39.935214
        // }, {
        //   'x': 116.350444,
        //   'y': 39.931645
        // }, {
        //   'x': 116.351684,
        //   'y': 39.91867
        // }, {
        //   'x': 116.353983,
        //   'y': 39.913855
        // }, {
        //   'x': 116.357253,
        //   'y': 39.923152
        // }, {
        //   'x': 116.349168,
        //   'y': 39.923152
        // }, {
        //   'x': 116.354954,
        //   'y': 39.935767
        // }, {
        //   'x': 116.36232,
        //   'y': 39.938339
        // }, {
        //   'x': 116.374249,
        //   'y': 39.94625
        // }, {
        //   'x': 116.380178,
        //   'y': 39.953053
        // }];
        client.query({
            query: GET_MAP_HOUSE,
            variables: {"lng": defaultX, "lat": defaultY, "zoom": defaultZoom}
        }).then(result => {
            let xys = result.data.MapHouseData.list;
            shouMapMarker(xys, map);
        });
    }

    render() {
        return (
            <div className='map-house'>
                <div className="map-house-title">
                    <Icon onClick={this.props.hideMap} name='angle left' size='large'/> 地图找房
                </div>
                <div className="map-house-content" id='allmap'></div>
            </div>
        );
    }
}

export default MapHouse;
