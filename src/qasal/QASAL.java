package qasal;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import java.awt.ComponentOrientation;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import org.apache.lucene.queryparser.classic.ParseException;

public final class QASAL extends JFrame {

    private static Stemmer stemmer;
    private static MaxentTagger tagger;

    public QASAL() {
        initComponents();
        logo.setIcon(new ImageIcon(Strings.logo));
        setLocationRelativeTo(null);
        answer.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        question.requestFocusInWindow();
        stemmer = new Stemmer();
        tagger = new MaxentTagger(Strings.TaggerFilesPath);
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        question = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        answer = new javax.swing.JTextArea();
        logo = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("QASAL");
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        question.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        question.setForeground(new java.awt.Color(96, 57, 19));
        question.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        question.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(96, 57, 19)), "السؤال", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Tahoma", 1, 12), new java.awt.Color(96, 57, 19))); // NOI18N
        question.setName(""); // NOI18N
        question.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                questionActionPerformed(evt);
            }
        });

        answer.setColumns(20);
        answer.setFont(new java.awt.Font("Arial", 1, 15)); // NOI18N
        answer.setForeground(new java.awt.Color(96, 57, 19));
        answer.setLineWrap(true);
        answer.setRows(5);
        answer.setWrapStyleWord(true);
        answer.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(96, 57, 19), 3));
        answer.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        jScrollPane1.setViewportView(answer);

        logo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(45, 45, 45)
                        .addComponent(logo, javax.swing.GroupLayout.PREFERRED_SIZE, 456, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(question, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1040, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(logo, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(question, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 640, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void questionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_questionActionPerformed
        QA();
    }//GEN-LAST:event_questionActionPerformed

    private void QA() {
        try {
            //clean text area
            answer.setText("");
            //remove symbols like '?'
            String Question = stemmer.removePunctuation(new StringBuffer(question.getText())).toString();
            //tokanizing question
            String[] tokanizing = Question.split(" ");
            //tag question
            String tagged = tagger.tagString(Question);
            //tokanizing tgged question
            String[] taggedWords = tagged.split(" ");
            String split[];
            String keyWordWithDash = "", keyWord = "";
            ArrayList<String> stemmedWords = new ArrayList<>();
            StringBuilder tagsDescription = new StringBuilder("");
            //display Tagger
            answer.append("\t Tagger \n");
            answer.append("\t " + tagged + "\n\n\n");
            //display Tokanize
            answer.append("\t Tokanize \n");
            for (String tok : tokanizing) {
                answer.append("\t " + tok + "\n");
            }
            answer.append("\n\n");
            for (String word : taggedWords) {
                //extract tag 
                split = word.split("/");
                //Tags Description
                tagsDescription.append("\t ").append(split[0]).append("\t\t\t").append(Tag(split[1])).append("\n");
                //Stem Words
                stemmedWords.add(stemmer.formatWord(split[0]));
                //Question Type
                if (questionType(split[1], split[0]) || stemmer.checkStopwords(split[0])) {
                    continue;
                }
                //keyWords
                if (keyWord(split[1])) {
                    keyWordWithDash += "_" + split[0];
                    keyWord += " " + split[0];
                }
            }
            if (keyWordWithDash.length() > 0) {
                keyWordWithDash = keyWordWithDash.substring(1);
                keyWord = keyWord.substring(1);
            }
            //display Tagged Words Description 
            answer.append("\t " + "Tags Description \n");
            answer.append(tagsDescription.toString());
            answer.append("\n\n");
            //display Stemmed words
            answer.append("\t " + "Stemmer \n");
            for (int i = 0; i < stemmedWords.size(); i++) {
                answer.append("\t " + tokanizing[i] + "\t\t\t" + stemmedWords.get(i) + "\n");
            }
            answer.append("\n\n");
            //display keywords
            answer.append("\t " + "Key Words \n");
            answer.append("\t " + keyWord + "\n");
            answer.append("\n\n");
            //semantic search
            ArrayList<String> otherValues = new ReadOWL().otherValues(keyWordWithDash);
            //query retrival
            ArrayList<String> queryRetrival = Lucene.Searcher(keyWord);
            queryRetrival.add(0, "\t\t\t====================\t" + keyWord + "\t====================\t\t");
            while (queryRetrival.size() > 6) {
                queryRetrival.remove(6);
            }
            otherValues.forEach((ov) -> {
                int count = 0;
                try {
                    ov = ov.replace("_", " ");
                    queryRetrival.add("\t\t\t====================\t" + ov + "\t====================\t\t");
                    for (String query : Lucene.Searcher(ov)) {
                        if (++count > 5) {
                            break;
                        }
                        queryRetrival.add(query);
                    }
                } catch (IOException | ParseException ex) {
                }
            });
            //Other Values
            if (!otherValues.isEmpty()) {
                answer.append("\t " + "Other Values \n");
                otherValues.forEach(ov -> {
                    answer.append("\t " + ov + "\n");
                });
                answer.append("\n\n");
            }
            //Query Retrival
            if (!queryRetrival.isEmpty()) {
                answer.append("\t " + "Query Retrival \n");
                queryRetrival.forEach(qr -> {
                    answer.append("\t " + qr + "\n");
                });
                answer.append("\n\n");
            }
            //Answers
            ArrayList<String> answers = new ReadOWL().semintic(keyWordWithDash);
            if (!answers.isEmpty()) {
                answer.append("\t " + "Answer From Ontology \n");
                answers.forEach((ans) -> {
                    answer.append("\t " + ans + "\n");
                });
                answer.append("\n\n");
            }
        } catch (ParseException | IOException ex) {
        }
    }

    private boolean questionType(String tag, String word) {
        if (tag.equals("WP") || tag.equals("WRB")) {
            answer.append("\t " + "Question Type \n");
            if (word.equals("اين") || word.equals("أين")) {
                answer.append("\t " + "سؤال عن المكان");
                answer.append("\n\n\n");
                return true;
            } else {
                answer.append("\t " + "Not Recognized In This Version" + "\n\n\n");
            }
        }
        return false;
    }

    private boolean keyWord(String tag) {
        return tag.equals("DTNN") || tag.equals("DTNNP") || tag.equals("DTNNPS") || tag.equals("DTNNS")
                || tag.equals("NN") || tag.equals("NNP") || tag.equals("NNPS") || tag.equals("NNS")
                || tag.equals("NOUN") || tag.equals("ADJ") || tag.equals("DTJJ") || tag.equals("DTJJR")
                || tag.equals("JJ") || tag.equals("JJR");
    }

    private String Tag(String tag) {
        switch (tag) {
            case "ADJ":
                return "adj";
            case "CC":
                return "Coordinating conjunction";
            case "CD":
                return "Cardinal number";
            case "DT":
                return "determiner";
            case "DTJJ":
                return "adjective with the determiner “Al” (ال)";
            case "DTJJR":
                return "adjective, comparative with the determiner “Al” (ال)";
            case "DTNN":
                return "noun, singular or mass with the determiner “Al” (ال)";
            case "DTNNP":
                return "Proper noun, singular with the determiner “Al” (ال)";
            case "DTNNPS":
                return "Proper noun, plural with the determiner “Al” (ال)";
            case "DTNNS":
                return "noun, plural with the determiner “Al” (ال)";
            case "IN":
                return "Preposition or subordinating conjunction";
            case "JJ":
                return "adjective";
            case "JJR":
                return "Adjective, comparative";
            case "NN":
                return "noun, singular or mass";
            case "NNP":
                return "Proper noun, singular";
            case "NNPS":
                return "Proper noun, plural";
            case "NNS":
                return "noun, plural";
            case "NOUN":
                return "noun";
            case "PRP":
                return "Personal pronoun";
            case "PRP$":
                return "Possessive pronoun";
            case "PUNC":
                return "punctuation";
            case "RB":
                return "adverb";
            case "RP":
                return "particle";
            case "UH":
                return "interjection";
            case "VB":
                return "verb, base form";
            case "VBD":
                return "Verb, past tense";
            case "VBG":
                return "verb, gerund or present participle";
            case "VBN":
                return "verb, past participle";
            case "VBP":
                return "Verb, non-3rd person singular present";
            case "VN":
                return "verb, past participle";
            case "WP":
                return "Wh-pronoun";
            case "WRB":
                return "Wh-adverb";
            default:
                return "UNKNOWEN";
        }
    }

    public static void main(String args[]) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
        }
        java.awt.EventQueue.invokeLater(() -> {
            new QASAL().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea answer;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel logo;
    private javax.swing.JTextField question;
    // End of variables declaration//GEN-END:variables
}
