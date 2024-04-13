// Category.ts
export default class Category {
    id: number;
    parentId: number | null;
    name: string;

    constructor(id: number, parentId: number | null, name: string) {
        this.id = id;
        this.parentId = parentId;
        this.name = name;
    }
    
}
