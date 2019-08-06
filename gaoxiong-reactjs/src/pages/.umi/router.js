import React from 'react';
import { Router as DefaultRouter, Route, Switch } from 'react-router-dom';
import dynamic from 'umi/dynamic';
import renderRoutes from 'umi/lib/renderRoutes';
import history from '@tmp/history';
import { routerRedux } from 'dva';

const Router = routerRedux.ConnectedRouter;

const routes = [
  {
    path: '/',
    component: require('../../layouts/index.js').default,
    routes: [
      {
        path: '/HelloWorld',
        exact: true,
        component: require('../HelloWorld.js').default,
      },
      {
        path: '/index_old',
        exact: true,
        component: require('../index_old.js').default,
      },
      {
        path: '/List',
        exact: true,
        component: require('../List.js').default,
      },
      {
        path: '/MyTabs',
        exact: true,
        component: require('../MyTabs.js').default,
      },
      {
        path: '/user/UserAdd',
        exact: true,
        component: require('../user/UserAdd.js').default,
      },
      {
        path: '/user/UserList',
        exact: true,
        component: require('../user/UserList.js').default,
      },
      {
        component: () =>
          React.createElement(
            require('C:/MyProject/haoke/gaoxiong-reactjs/node_modules/umi-build-dev/lib/plugins/404/NotFound.js')
              .default,
            { pagesPath: 'src/pages', hasRoutesInConfig: false },
          ),
      },
    ],
  },
  {
    component: () =>
      React.createElement(
        require('C:/MyProject/haoke/gaoxiong-reactjs/node_modules/umi-build-dev/lib/plugins/404/NotFound.js')
          .default,
        { pagesPath: 'src/pages', hasRoutesInConfig: false },
      ),
  },
];
window.g_routes = routes;
const plugins = require('umi/_runtimePlugin');
plugins.applyForEach('patchRoutes', { initialValue: routes });

export { routes };

export default class RouterWrapper extends React.Component {
  unListen = () => {};

  constructor(props) {
    super(props);

    // route change handler
    function routeChangeHandler(location, action) {
      plugins.applyForEach('onRouteChange', {
        initialValue: {
          routes,
          location,
          action,
        },
      });
    }
    this.unListen = history.listen(routeChangeHandler);
    routeChangeHandler(history.location);
  }

  componentWillUnmount() {
    this.unListen();
  }

  render() {
    const props = this.props || {};
    return <Router history={history}>{renderRoutes(routes, props)}</Router>;
  }
}
