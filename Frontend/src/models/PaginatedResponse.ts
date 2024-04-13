class PaginatedResponse<T> {
    content: T[];
    totalPages: number;
    totalElements: number;
    size: number;
    number: number; // current page index

    constructor(
        content: T[],
        totalPages: number,
        totalElements: number,
        size: number,
        number: number
    ) {
        this.content = content;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.size = size;
        this.number = number;
    }
}

export default PaginatedResponse;