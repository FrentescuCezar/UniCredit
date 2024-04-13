import React, { useState, useEffect } from 'react';
import { Pagination } from '../Utils/Pagination';
import Transaction from '../../models/Transaction';
import Category from '../../models/Category';
import Modal from 'react-modal';


const TransactionPage: React.FC = () => {
    const [transactions, setTransactions] = useState<Transaction[]>([]);
    const [currentPage, setCurrentPage] = useState<number>(1);
    const [totalPages, setTotalPages] = useState<number>(0);
    const [modalIsOpen, setModalIsOpen] = useState<boolean>(false);
    const [categories, setCategories] = useState<Category[]>([]);
    const [selectedTransaction, setSelectedTransaction] = useState<Transaction | null>(null);
    const [categoryHistory, setCategoryHistory] = useState<number[]>([]);



    useEffect(() => {
        const fetchTransactions = async () => {
            try {
                const response = await fetch(`http://localhost:8081/api/transactions/pageable?page=${currentPage - 1}&size=10`);
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                const data = await response.json();
                const transactionsWithCategory = await Promise.all(data.content.map(async (transactionData: any) => {
                    const transaction = new Transaction(
                        transactionData.id,
                        transactionData.date,
                        transactionData.amount,
                        transactionData.description,
                        transactionData.categoryId,
                        transactionData.keywordId,
                        transactionData.parentId
                    );
                    if (transaction.categoryId) {
                        try {
                            const categoryResponse = await fetch(`http://localhost:8082/api/categories/${transaction.categoryId}`);
                            if (!categoryResponse.ok) {
                                throw new Error('Failed to fetch category');
                            }
                            const categoryData = await categoryResponse.json();
                            const category = new Category(categoryData.id, null, categoryData.value);


                            transaction.categoryName = category.name;
                        } catch (error) {
                            console.error('Error fetching category:', error);
                            transaction.categoryName = 'Category Unavailable';
                        }
                    } else {
                        transaction.categoryName = 'No Category';
                    }

                    return transaction;
                }));

                setTransactions(transactionsWithCategory);
                setTotalPages(data.totalPages);
            } catch (error) {
                console.error('Failed to fetch transactions:', error);
            }
        };

        fetchTransactions();
    }, [currentPage, transactions]);

    const paginate = (pageNumber: number) => setCurrentPage(pageNumber);

    const handleFindCategory = async (transaction: Transaction) => {
        const { id, date, amount, categoryId, keywordId, parentId, description } = transaction;
        try {
            const response = await fetch(`http://localhost:8082/api/categories/autoUpdateCategory/${id}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    date,
                    amount,
                    categoryId,
                    keywordId,
                    parentId,
                    description
                })
            });
            if (!response.ok) {
                throw new Error('Failed to update transaction category');
            }
            const updatedTransaction: Transaction = await response.json();
            setTransactions(transactions.map(t => t.id === updatedTransaction.id ? updatedTransaction : t));
        } catch (error) {
            console.error('Error updating transaction category:', error);
        }
    };



    const openModal = (transaction: Transaction) => {
        fetchCategories();
        setSelectedTransaction(transaction);
        setModalIsOpen(true);
    };

    const closeModal = () => {
        setModalIsOpen(false);
    };

    const fetchCategories = async (parentId?: number) => {
        const url = parentId === undefined ?
            'http://localhost:8082/api/categories/categories/top-level' :
            `http://localhost:8082/api/categories/categories/by-parent?parentId=${parentId}`;
        try {
            const response = await fetch(url);
            const categoryData: { id: number, parentId: number | null, value: string }[] = await response.json();
            const categories = categoryData.map(cat => new Category(cat.id, cat.parentId, cat.value));
            setCategories(categories);

            if (parentId !== undefined) {
                setCategoryHistory(prevHistory => [...prevHistory, parentId]);
            } else {
                setCategoryHistory([]);
            }

        } catch (error) {
            console.error('Failed to fetch categories:', error);
        }
    };

    const goBackToPreviousCategory = () => {
        setCategoryHistory(prevHistory => {
            const newHistory = [...prevHistory];
            newHistory.pop();
            const parentId = newHistory[newHistory.length - 1] || undefined;
            fetchCategories(parentId);
            return newHistory;
        });
    };


    const updateTransactionCategory = async (categoryId: number) => {
        if (!selectedTransaction) return;
        const url = `http://localhost:8082/api/categories/updateTransactionCategory?transactionId=${selectedTransaction.id}&categoryId=${categoryId}`;
        const response = await fetch(url, { method: 'POST' });
        const updatedTransaction = await response.json();
        setTransactions(transactions.map(t => t.id === updatedTransaction.id ? updatedTransaction : t));
        closeModal();
    };



    return (
        <div>
            <h1>Transactions</h1>
            <div className="row">
                {transactions.map((transaction, index) => (
                    <div key={transaction.id} className="col-lg-4 col-md-6 mb-3">
                        <div className="card transaction-card">
                            <div className="transaction-header">
                                Category: {transaction.categoryName || 'Loading...'}
                            </div>
                            <div className="card-body">
                                <h5 className="card-title">{transaction.description}</h5>
                                <h6 className="card-subtitle mb-2 text-muted">{transaction.date}</h6>
                                <p className="card-text card-text-small">
                                    Amount: {transaction.amount.toFixed(2)}
                                </p>
                                <div className="d-flex justify-content-end">
                                    <button className="btn btn-primary btn-sm m-1" onClick={() => handleFindCategory(transaction)}>Find Category</button>
                                    <button className="btn btn-warning btn-sm m-1" onClick={() => openModal(transaction)}>Edit</button>
                                    <button className="btn btn-danger btn-sm m-1">Delete</button>
                                </div>
                            </div>
                        </div>
                    </div>
                ))}
            </div>
            <Pagination currentPage={currentPage} totalPages={totalPages} paginate={paginate} />

            <Modal
                isOpen={modalIsOpen}
                onRequestClose={closeModal}
                contentLabel="Category Selection Modal"
                className="Modal"
                overlayClassName="Overlay"
            >
                <div className="modal-header">
                    <h5 className="modal-title">Select a Category</h5>
                    <button type="button" className="btn-close" onClick={closeModal}></button>
                </div>
                <div className="modal-body">
                    {categories.map((category) => (
                        <div key={category.id} className="card mb-3">
                            <div className="card-body">
                                <h5 className="card-title">{category.name}</h5>
                                <div className="card-text">
                                    <button className="btn   btn-warning  btn-sm m-1" onClick={() => fetchCategories(category.id)}>More subcategories</button>
                                    <button className="btn  btn-primary btn-sm m-1" onClick={() => updateTransactionCategory(category.id)}>Select</button>
                                </div>
                            </div>
                        </div>
                    ))}
                </div>
                <div className="modal-footer">
                    {categoryHistory.length > 0 && (
                        <button type="button" className="btn btn-secondary" onClick={goBackToPreviousCategory}>
                            Back
                        </button>
                    )}
                    <button type="button" className="btn btn-secondary" onClick={closeModal}>Close</button>
                </div>
            </Modal>
        </div>
    );
}

export default TransactionPage;
