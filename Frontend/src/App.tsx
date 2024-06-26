import './App.css';
import { Navbar } from './layouts/NavbarAndFooter/Navbar';
import { Footer } from './layouts/NavbarAndFooter/Footer';

import { Homepage } from './layouts/Homepage/Homepage';


import { Route, Redirect, Switch, useHistory } from 'react-router-dom';

import { oktaConfig } from './lib/oktaConfig';
import { OktaAuth, toRelativeUrl } from '@okta/okta-auth-js';
import { LoginCallback, Security } from '@okta/okta-react';
import LoginWidget from './Auth/Login';
import Transaction from './models/Transaction';
import TransactionPage from './layouts/Transactions/TransactionsPage';




const oktaAuth = new OktaAuth(oktaConfig)


export const App = () => {


  const customAuthHandler = () => {
    history.push('/login');
  }
  const history = useHistory();

  const restoreOriginalUri = async (_oktaAuth: any, originalUri: string) => {
    history.replace(toRelativeUrl(originalUri || '/', window.location.origin));
  }


  return (
    <div className="d-flex flex-column min-vh-100">
      <Security oktaAuth={oktaAuth} restoreOriginalUri={restoreOriginalUri} onAuthRequired={customAuthHandler}>
        <Navbar />

        <div className='flex-grow-1'>
          <Switch>
            <Route path='/' exact>
              <Redirect to='/home' />
            </Route>

            <Route path='/home' exact>
              <Homepage />
            </Route>

            <Route path='/tranzactii' exact>
              <TransactionPage />
            </Route>

            <Route path='/login' render={() =>
              <LoginWidget config={oktaConfig} />}
            />

            <Route path='/login/callback' component={LoginCallback} />




            <Route exact path='/user/:userName/:userName' render={({ match }) => (
              <Redirect to={`/user/${match.params.userName}`} />
            )} />




          </Switch>
        </div>

        <Footer />
      </Security>
    </div>
  );
};
