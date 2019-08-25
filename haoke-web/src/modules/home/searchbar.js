import React from 'react';
import {Icon, Item,Pagination} from 'semantic-ui-react'
import "./search.css";

class SearchBar extends React.Component {
    hideSearchBar =()=>{
        this.props.hideSearchBar();
    }


    handlePageChange = (e, { activePage }) =>{
        this.props.searchPage(null,{
            page:activePage
        });
    }

    render() {
        return <div className='search-bar'>
            <Icon onClick={this.hideSearchBar} name='angle left' size='large'/>
            {
                this.props.totalPage > 1 ? (
                    <Pagination
                        defaultActivePage={1}
                        firstItem={null}
                        lastItem={null}
                        totalPages={this.props.totalPage}
                        onPageChange={this.handlePageChange}
                    />
                ) : null
            }
            <div className="search-bar-content">
                <Item.Group divided unstackable>
                    {
                        this.props.searchData.map(item => {
                            return (
                                <Item key={item.id}>
                                    <Item.Image
                                        src={"http://pw0lsxiue.bkt.clouddn.com/" + item.image}/>
                                    <Item.Content>
                                        <Item.Header>
                                            {/*<div className='house-title' >*/}
                                            {/*{item.title}</div>*/}
                                            <div className='house-title'
                                                 dangerouslySetInnerHTML={{__html: item.title}}></div>
                                        </Item.Header>
                                        <Item.Meta>
                                    <span className='cinema'>
                                    {item.orientation}/{item.rentMethod}/{item.houseType}</span>
                                        </Item.Meta>
                                        <Item.Description>
                                            上海
                                        </Item.Description>
                                        <Item.Description>{item.rent}
                                        </Item.Description>
                                    </Item.Content>
                                </Item>
                            )
                        })
                    }

                </Item.Group>
            </div>
        </div>;
    }
}

export default SearchBar;
