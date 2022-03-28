import flask
from flask import request
from flask import json
from flask import Response
import csv
import pandas as pd
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.metrics.pairwise import linear_kernel

app = flask.Flask(__name__)
app.config["DEBUG"] = True

movies = pd.read_csv('/home/kutay/IdeaProjects/InfoqCrawler/src/main/resources/articles.csv', sep='\t', encoding='latin-1', usecols=['article_id','title','main_topic', 'author', 'related_topics'])

movies['related_topics'] = movies['related_topics'].str.split('|')

# Convert genres to string value
movies['related_topics'].fillna("").astype('str')
movies['related_topics'] = [str(item) for item in movies['related_topics']]

tf = TfidfVectorizer(analyzer='word',ngram_range=(1, 2),min_df=0, stop_words='english')
tfidf_matrix = tf.fit_transform(movies['related_topics'])
tfidf_matrix.shape

cosine_sim = linear_kernel(tfidf_matrix, tfidf_matrix)
cosine_sim[:4, :4]

titles = movies['title']
indices = pd.Series(movies.index, index=movies['title'])

def genre_recommendations(title):
    idx = indices[title]
    sim_scores = list(enumerate(cosine_sim[idx]))
    sim_scores = sorted(sim_scores, key=lambda x: x[1], reverse=True)
    # Similarity puani en yuksek ilk 5 makale doner
    sim_scores = sim_scores[1:6]
    return sim_scores
    # movie_indices = [i[0] for i in sim_scores]
    # return titles.iloc[movie_indices]

# Content-Type -> text/plain
@app.route('/api/recommend', methods = ['GET'])
def return_recoms():
    # title = ""
    list = []

    title = request.args.get('title')

    list = genre_recommendations(title)

    data = {
        'list' : list
    }

    js = json.dumps(data)
    resp = Response(js, status = 200, mimetype = 'application/json')
    return resp

if __name__ == '__main__':
    app.run()