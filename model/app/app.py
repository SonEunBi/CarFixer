import os
import json
import urllib
import h5py
import pickle as pk
import numpy as np

from flask import Flask, request

import engine # remember to reinclude this

# A <form> tag is marked with enctype=multipart/form-data and an <input type=file> is placed in that form.
# The application accesses the file from the files dictionary on the request object.
# use the save() method of the file to save the file permanently somewhere on the filesystem.

app = Flask(__name__)

@app.route('/assessment', methods=['GET', 'POST'])
def upload_and_classify():
	if request.method == 'GET':
		url = request.args.get('url')
		model_results = engine.engine(url)
		return json.dumps(model_results);


if __name__ == '__main__':
	app.run(host='0.0.0.0', port=8080, debug=True, use_reloader=False) # remember to set back to False	