import { useOktaAuth } from "@okta/okta-react";
import { Link } from "react-router-dom";

export const UnicreditService = () => {

    const { authState } = useOktaAuth();

    return (
        <div className='container my-5'>
            <div className='row p-4 align-items-center border shadow-lg'>
                <div className='col-lg-7 p-3 text-content'>
                    <h1 className='display-5 fw-bold'>
                        Personalizează-ți tranzacțiile pentru o mai bună gestionare financiară.
                    </h1>
                    <p className='lead'>
                        Pentru a vedea tranzacțiile tale și a le putea categoriza, <br></br> te rugăm să te autentifici pe platforma noastră.
                    </p>
                    <div className='d-grid gap-2 justify-content-md-start mb-4 mb-lg-3'>
                        {!authState?.isAuthenticated
                            ?
                            <Link className='btn main-color-gray-button btn-lg' to='/login'>
                                Autentifică-te
                            </Link>
                            :
                            <Link type='button' className='btn main-color-gray-button btn-lg' to='/monbuilder'>
                                Verifică-ți tranzacțiile
                            </Link>
                        }

                    </div>
                </div>
                <div className='col-lg-6 lost-image'></div>
            </div>
        </div>
    )
}