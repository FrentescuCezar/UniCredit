// Navbar.tsx
import React from 'react';
import { Link, NavLink } from 'react-router-dom';

import { useOktaAuth } from '@okta/okta-react';
import { SpinnerLoading } from '../Utils/SpinnerLoading';
import NavBarUserCircle from './NavBarUserCircle';



interface JwtPayload {
    sub: string;

}


export const Navbar: React.FC<{}> = (props) => {


    const { oktaAuth, authState } = useOktaAuth();

    if (!authState) {
        return <SpinnerLoading />
    }

    const handleLogout = async () => oktaAuth.signOut();


    return (

        <nav className="navbar navbar-expand-lg shadow-sm py-2">
            <div className="container">
                <NavLink className="navbar-brand" to="/home">
                    <img
                        src={require('./../../Images/PublicImages/Logo.png')}
                        alt="FIICredit"
                        style={{ height: '35px' }}
                    />
                </NavLink>

                {/* 3 Lines Button for mobile */}
                <button className="navbar-toggler navbar-toggler-mobile btn-black" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNavDropdown" aria-controls="navbarNavDropdown" aria-expanded="false" aria-label="Toggle navigation">
                    <span className="navbar-toggler-icon"></span>
                </button>

                <div className="collapse navbar-collapse" id="navbarNavDropdown">
                    <ul className="navbar-nav me-auto my-2 my-lg-0">
                        <li className="nav-item" style={{ marginLeft: '50px' }}>
                            <NavLink className="nav-link btn-black-bold" to="/home">Acasă</NavLink>
                        </li>

                        <li className="nav-item" style={{ marginLeft: '10px' }}>
                            {authState.isAuthenticated
                                ?
                                <NavLink className="nav-link btn-black-bold" to="/tranzactii">Tranzactii</NavLink>
                                :
                                <NavLink className="nav-link btn-black-bold" to="/login">Tranzactii</NavLink>
                            }
                        </li>
                    </ul>
                    {!authState.isAuthenticated
                        ?
                        <Link type='button' className="btn btn-outline-light mx-5 btn-red-hover" to='/login'>Autentifică-te</Link>
                        :
                        //<button className='btn btn-outline-dark mx-5 btn-red-hover' onClick={handleLogout}>Logout</button>
                        <NavBarUserCircle username={"testUser"} onLogout={handleLogout} authState={authState} />
                    }
                </div>
            </div>
        </nav>
    );
};
