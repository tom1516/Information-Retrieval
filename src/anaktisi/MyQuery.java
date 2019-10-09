/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package anaktisi;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.search.postingshighlight.PostingsHighlighter;
import org.apache.lucene.search.spell.PlainTextDictionary;
import org.apache.lucene.search.spell.SpellChecker;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

/**
 *
 * @author user
 */
public class MyQuery extends javax.swing.JFrame {

     private Directory index;
     private int flag=1;
    
    /**
     * Creates new form Query
     * @param reviews
     * @throws java.io.IOException
     */
    public MyQuery( ArrayList<String> reviews,ArrayList<String> stars) throws IOException {
        initComponents();
        MyIndex ind = new  MyIndex(reviews);
        this.index=new RAMDirectory();
       
         try {
             this.index=ind.makeIndex(stars);
         } catch (ParseException ex) {
             Logger.getLogger(MyQuery.class.getName()).log(Level.SEVERE, null, ex);
         }
    }

    private MyQuery() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        queryText = new javax.swing.JTextField();
        searchButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        searchButton.setText("Αναζήτηση");
        searchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchButtonActionPerformed(evt);
            }
        });

        jLabel1.setText("Εισάγετε το ερώτημα ");

        jList1.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(jList1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(145, 145, 145)
                        .addComponent(searchButton))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 299, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(82, 82, 82)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(queryText, javax.swing.GroupLayout.PREFERRED_SIZE, 231, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(48, 48, 48)
                                .addComponent(jLabel1)))))
                .addContainerGap(52, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(queryText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(searchButton)
                .addGap(29, 29, 29))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void addSuggestions() throws IOException{
        
        String str=this.queryText.getText();
        StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_40);
        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_40, analyzer);
        
        Directory directory = FSDirectory.open(new File("."));
        SpellChecker sp = new SpellChecker(directory);


        //index the dictionary
        sp.indexDictionary(new PlainTextDictionary(new File("fulldictionary00.txt")),config,false);

        String[] tokens = str.split(" ");
        Vector<String> v = new Vector<String>();
                

        for(int i=0;i<tokens.length;i++){
            String search = tokens[i];
            int suggestionNumber = 5;
            String[] suggestions = sp.suggestSimilar(search, suggestionNumber);

            for (String word : suggestions) {
                    v.add(word);
            }
            v.add("---------");
        }
        this.jList1.setListData(v);
    }
    private void searchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchButtonActionPerformed
        // TODO add your handling code here:
        if(flag==1){
            flag=0;
            try {
                this.addSuggestions();
            } catch (IOException ex) {
                Logger.getLogger(MyQuery.class.getName()).log(Level.SEVERE, null, ex);
            }
            return ;
        }
        if(this.queryText.getText().length()>0){
            try {
                String str = this.queryText.getText();
                
                //addSuggestions();
                
                StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_40);
                Query q = null;
                try {
                    q = new QueryParser(Version.LUCENE_40, "text", analyzer).parse(str);
                } catch (ParseException ex) {
                    Logger.getLogger(MyQuery.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                int hitsPerPage = 100;
                IndexReader reader = null;
                try {
                    reader = DirectoryReader.open(index);
                } catch (IOException ex) {
                    Logger.getLogger(MyQuery.class.getName()).log(Level.SEVERE, null, ex);
                }
                IndexSearcher searcher = new IndexSearcher(reader);
                TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage, true);
                try {
                    searcher.search(q, collector);
                } catch (IOException ex) {
                    Logger.getLogger(MyQuery.class.getName()).log(Level.SEVERE, null, ex);
                }         
                ScoreDoc[] hits = collector.topDocs().scoreDocs;
                PostingsHighlighter highlighter = new PostingsHighlighter();
                TopDocs topDocs = searcher.search(q, 100);
                String[] strings = highlighter.highlight("text", q, searcher,topDocs);
                
                               
                // 4. display results
                str="";
               
                for(int i=0;i<hits.length;++i) {
                    int docId = hits[i].doc;
                    Document d = null;
                    try {
                        d = searcher.doc(docId);
                    } catch (IOException ex) {
                        Logger.getLogger(MyQuery.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    str= str +(i + 1) + ". " +d.get("text") +"\n";
                }
                try {
                    // reader can only be closed when there
                    // is no need to access the documents any more.
                    reader.close();
                } catch (IOException ex) {
                    Logger.getLogger(MyQuery.class.getName()).log(Level.SEVERE, null, ex);
                }
                Results res = new Results();
                res.set(str); //
                for (String string : strings) {
                    res.add(string);
                }
                res.setVisible(true);
            } catch (IOException ex) {
                Logger.getLogger(MyQuery.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        flag=1;
        
    }//GEN-LAST:event_searchButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MyQuery.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MyQuery.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MyQuery.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MyQuery.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MyQuery().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JList jList1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField queryText;
    private javax.swing.JButton searchButton;
    // End of variables declaration//GEN-END:variables
}