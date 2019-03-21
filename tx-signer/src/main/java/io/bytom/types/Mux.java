package io.bytom.types;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class Mux extends Entry {

    private ValueSource[] sources;

    private Program program;

    private List<ValueDestination> witnessDestinations = new ArrayList<>();

    public Mux() {}

    public Mux(ValueSource[] sources, Program program) {
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

    public ValueSource[] getSources() {
        return sources;
    }

    public void setSources(ValueSource[] sources) {
        this.sources = sources;
    }

    public Program getProgram() {
        return program;
    }

    public void setProgram(Program program) {
        this.program = program;
    }

    public List<ValueDestination> getWitnessDestinations() {
        return witnessDestinations;
    }

    public void setWitnessDestinations(List<ValueDestination> witnessDestinations) {
        this.witnessDestinations = witnessDestinations;
    }
}
