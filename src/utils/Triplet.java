/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

/**
 *
 * @author matti
 */
public class Triplet<U, V, T>
{
    public U first;       // il primo campo di una terzina
    public V second;      // il secondo campo di una terzina
    public T third;       // il terzo campo di una terzina
 
    // Costruisce una nuova tripletta con i valori indicati
    public Triplet(U first, V second, T third)
    {
        this.first = first;
        this.second = second;
        this.third = third;
    }
 
    @Override
    public boolean equals(Object o)
    {
        /* Verifica che l'oggetto specificato sia "uguale a" l'oggetto corrente o meno */
 
        if (this == o) {
            return true;
        }
 
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
 
        Triplet triplet = (Triplet) o;
        // chiama il metodo `equals()` degli oggetti sottostanti
        if (!(first == triplet.first || triplet.first.getClass() == any.class || first.equals(triplet.first)) ||
                !(second == triplet.second || triplet.second.getClass() == any.class || second.equals(triplet.second)) ||
                !(third == triplet.third || triplet.third.getClass() == any.class || third.equals(triplet.third)) ) {
            return false;
        }
 
        return true;
    }
 
    @Override
    public int hashCode()
    {
        /* Calcola il codice hash per un oggetto utilizzando i codici hash di
        gli oggetti sottostanti */
 
        int result = first.hashCode();
        result = 31 * result + second.hashCode();
        result = 31 * result + third.hashCode();
        return result;
    }
 
    @Override
    public String toString() {
        return "(" + first + ", " + second + ", " + third + ")";
    }
 
    // Metodo di fabbrica per creare un'istanza immutabile tipizzata di tripletta
    public static <U, V, T> Triplet <U, V, T> of(U a, V b, T c) {
        return new Triplet <>(a, b, c);
    }

    public Triplet<U, any, any> getEqualFirst(){
        return Triplet.of(first, new any(), new any());
    }
    
    public Triplet<any, V, any> getEqualSecond(){
        return Triplet.of(new any(), second, new any());
    }
    
    public Triplet<any, any, T> getEqualThird(){
        return Triplet.of(new any(), new any(), third);
    }
    
    private class any{
        @Override
        public boolean equals(Object o)
        {
            return true;
        }
    }
    
    public U getFirst() {
        return first;
    }

    public void setFirst(U first) {
        this.first = first;
    }

    public V getSecond() {
        return second;
    }

    public void setSecond(V second) {
        this.second = second;
    }

    public T getThird() {
        return third;
    }

    public void setThird(T third) {
        this.third = third;
    }
    
}
 
