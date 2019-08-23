import React from 'react';
import {Icon, Item} from 'semantic-ui-react'

class SearchBar extends React.Component {
    render() {
        return (
            <div className='search-bar'>
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
                                                <div className='house-title'>
                                                    {item.title}</div>
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
            </div>
        );
    }
}

export default SearchBar;
