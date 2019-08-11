import React from 'react'
import {Modal, Button, Carousel} from 'antd';
//走马灯 图片 效果

class ShowPics extends React.Component{
    info =()=>{
      Modal.info({
        title:'',
        iconType:'false',
        width: '800px',
        okText: 'OK',
        content: (
          <div style={{width:650,height:400,textAlign:"center"}}>
            <Carousel autoplay={true}>
              {
                this.props.pics.split(",").map((value,index)=>{
                  return <div>
                    <img style={{maxWidth:600,maxHeight:400,margin:"0 auto"}} src={value}/>
                  </div>
                })
              }
            </Carousel>
          </div>
        ),
        onOk()  {},
      });
    };

  constructor(props) {
    super(props);
    this.state={
      //如果当前房源没有图片按钮不可用
      btnDisabled: this.props.pics ? false : true
    }
  }


  render() {
    return (
      <div>
        <Button aria-disabled={this.state.btnDisabled} icon={"picture"} shape={"circle"}
        onClick={()=>{this.info()}}/>
      </div>
    )
  }
}

export default ShowPics;
