package qasal;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.ar.ArabicAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.FSDirectory;

public class Lucene {

    public static void DataSpliter() {
        try {
            FileUtils.cleanDirectory(new File(Strings.DATA_PATH));
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(Strings.DataFilesPath)), StandardCharsets.UTF_8));
            int fileName = 0;
            String script;
            while ((script = reader.readLine()) != null) {
                for (String line : script.split("[(0-9)]")) {
                    if (!(line.equals("") || line.equals(" ") || line.equals("\n"))) {
                        File file = new File(Strings.DATA_PATH + "\\" + fileName++ + ".txt");
                        file.createNewFile();
                        new BufferedWriter(new FileWriter(file)).write(line);
                    }
                }
            }
        } catch (IOException ex) {
        }
    }

    public static void Indexer() {
        try (IndexWriter writer = new IndexWriter(FSDirectory.open(Paths.get(Strings.INDEX)),
                new IndexWriterConfig(new ArabicAnalyzer()).setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND))) {
            Files.walkFileTree(Paths.get(Strings.DATA_PATH), new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Document doc = new Document();
                    doc.add(new StringField(Strings.FILE_PATH, file.toString(), Field.Store.YES));
                    doc.add(new TextField(Strings.CONTENTS, new BufferedReader(new InputStreamReader(Files.newInputStream(file), StandardCharsets.UTF_8))));
                    writer.addDocument(doc);
                    return FileVisitResult.CONTINUE;
                }
            });
            writer.forceMerge(1);
        } catch (IOException ex) {
        }
    }

    public static ArrayList<String> Searcher(String query) throws ParseException, IOException {
        IndexSearcher searcher = new IndexSearcher(DirectoryReader.open(FSDirectory.open(Paths.get(Strings.INDEX))));
        ArrayList<String> answers = new ArrayList<>();
        Document doc;
        ArrayList<Float> scores = new ArrayList<>();
        String path = "";
        for (ScoreDoc scoreDoc : searcher.search(new QueryParser(Strings.CONTENTS, new ArabicAnalyzer()).parse(query), Strings.MAX_SEARCH).scoreDocs) {
            doc = searcher.doc(scoreDoc.doc);
            path = doc.get(Strings.FILE_PATH);
            scores.add(scoreDoc.score);
            answers.add(new BufferedReader(new InputStreamReader(new FileInputStream(path), StandardCharsets.UTF_8)).readLine());
        }
        return sort(scores, answers);
    }

    private static ArrayList<String> sort(ArrayList<Float> score, ArrayList<String> answers) {
        for (int i = 0; i < score.size() - 1; i++) {
            int index = i;
            for (int j = i + 1; j < score.size(); j++) {
                if (score.get(j) < score.get(index)) {
                    index = j;
                }
            }
            float smallerNumber = score.get(index);
            score.set(index, score.get(i));
            score.set(i, smallerNumber);
            String answer = answers.get(index);
            answers.set(index, answers.get(i));
            answers.set(i, answer);
        }
        return answers;
    }
}
