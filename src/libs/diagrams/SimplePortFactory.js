import { AbstractPortFactory } from "@projectstorm/react-diagrams";

class SimplePortFactory extends AbstractPortFactory {
    constructor(type, cb) {
        super(type);
        this.cb = cb;
    }

    getNewInstance(initialConfig) {
        return this.cb(initialConfig);
    }
}

module.exports = SimplePortFactory;
