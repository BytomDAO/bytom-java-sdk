package io.bytom.types;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class Mux extends Entry {

    public ValueSource[] sources;

    public Program program;

    public List<ValueDestination> witnessDestinations = new ArrayList<>();

    public Mux() {
    }

    public Mux(ValueSource[] sources, Program program) {
        this();
        this.sources = sources;
        this.program = program;
    }

    @Override
    public String typ() {
        return "mux1";
    }

    @Override
    public void writeForHash(ByteArrayOutputStream out) {
        mustWriteForHash(out, this.sources);
        mustWriteForHash(out, this.program);
    }
}
