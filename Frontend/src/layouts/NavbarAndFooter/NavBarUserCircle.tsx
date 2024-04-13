import React, { useEffect } from 'react';
import { Dropdown } from 'react-bootstrap';
import { Link } from 'react-router-dom';

import usernamePhoto from '../../Images/PublicImages/usernamePhoto.png'

interface UserCircleProps {
    onLogout: () => void;
    username: string;
    authState: any;
}


const UserCircle: React.FC<UserCircleProps> = ({ onLogout, username, authState }) => {

    const [responseDataLength, setResponseDataLength] = React.useState<number>(0);


    let backgroundImageStyle;
    if (responseDataLength !== 0) {
        backgroundImageStyle = { backgroundImage: usernamePhoto };
    } else {
        backgroundImageStyle = { backgroundImage: usernamePhoto };
    }


    return (
        <Dropdown>
            <div className="circle" style={backgroundImageStyle}>
                <Dropdown.Toggle id="user-circle" className="circle-toggle" />
            </div>
            <Dropdown.Menu align="end">
                <Link to={`/user/${username}/${username}`}>
                    <Dropdown.Item as="span">User Profile</Dropdown.Item>
                </Link>
                <Dropdown.Item onClick={onLogout}>Logout</Dropdown.Item>
            </Dropdown.Menu>
        </Dropdown>
    );
};

export default UserCircle;