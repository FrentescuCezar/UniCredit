import React, { useState, useEffect } from 'react';
import { Pagination } from '../Utils/Pagination';
import Transaction from '../../models/Transaction';
import Category from '../../models/Category'; // Import the Category class

const TransactionPage: React.FC = () => {
    const [transactions, setTransactions] = useState<Transaction[]>([]);
    const [currentPage, setCurrentPage] = useState<number>(1);
    const [totalPages, setTotalPages] = useState<number>(0);

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
                    console.log(transaction);
                    if (transaction.categoryId) {
                        try {
                            const categoryResponse = await fetch(`http://localhost:8082/api/categories/${transaction.categoryId}`);
                            if (!categoryResponse.ok) {
                                throw new Error('Failed to fetch category');
                            }
                            const categoryData = await categoryResponse.json();
                            const category = new Category(categoryData.id, categoryData.value);
                            transaction.categoryName = category.name;
                        } catch (error) {
                            console.error('Error fetching category:', error);
                            transaction.categoryName = 'Category Unavailable'; // Set default or error-specific category name
                        }
                    } else {
                        transaction.categoryName = 'No Category'; // Set a default value for transactions without a categoryId
                    }

                    return transaction;
                }));

                setTransactions(transactionsWithCategory);
                setTotalPages(data.totalPages);
            } catch (error) {
                console.error('Failed to fetch transactions:', error);
                // Handle the error state as appropriate
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
                                    <button className="btn btn-warning btn-sm m-1">Edit</button>
                                    <button className="btn btn-danger btn-sm m-1">Delete</button>
                                </div>
                            </div>
                        </div>
                    </div>
                ))}
            </div>
            <Pagination currentPage={currentPage} totalPages={totalPages} paginate={paginate} />
        </div>
    );
}

export default TransactionPage;
