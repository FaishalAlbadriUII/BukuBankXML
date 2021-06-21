/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

/**
 * @author Ahmad Faishal Albadri
 * @nim 20523166
 * 
 * @author Muhammad Yusuf Hidayat
 * @nim 20523128
 */
public class DataBank {
    
    private int id, debit, credit;
    private String desc;

    public DataBank() {
    }

    public DataBank(int id, int debit, int credit, String desc) {
        this.id = id;
        this.debit = debit;
        this.credit = credit;
        this.desc = desc;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDebit() {
        return debit;
    }

    public void setDebit(int debit) {
        this.debit = debit;
    }

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
