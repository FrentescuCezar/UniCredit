class Transaction {
    id: number;
    date: string; // You could also use the Date type if you convert the date strings to Date objects when you receive them
    amount: number;
    categoryId?: number;
    keywordId?: number;
    parentId?: number;
    description: string;
    categoryName?: string; // Optional: will be added dynamically


    constructor(
        id: number,
        date: string,
        amount: number,
        description: string,
        categoryId?: number,
        keywordId?: number,
        parentId?: number,
        categoryName?: string

    ) {
        this.id = id;
        this.date = date;
        this.amount = amount;
        this.categoryId = categoryId;
        this.keywordId = keywordId;
        this.parentId = parentId;
        this.description = description;
        this.categoryName = categoryName;

    }

    // Example of a method you might add to the class
    displayTransaction(): string {
        return `${this.date} - ${this.amount.toFixed(2)} - ${this.description}`;
    }
}

export default Transaction;
