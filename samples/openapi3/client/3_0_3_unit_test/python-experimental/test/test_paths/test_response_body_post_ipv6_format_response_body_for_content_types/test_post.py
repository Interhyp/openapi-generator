# coding: utf-8

"""


    Generated by: https://openapi-generator.tech
"""

import unittest
from unittest.mock import patch

import urllib3

import unit_test_api
from unit_test_api.paths.response_body_post_ipv6_format_response_body_for_content_types import post  # noqa: E501
from unit_test_api import configuration, schemas, api_client

from .. import ApiTestMixin


class TestResponseBodyPostIpv6FormatResponseBodyForContentTypes(ApiTestMixin, unittest.TestCase):
    """
    ResponseBodyPostIpv6FormatResponseBodyForContentTypes unit test stubs
    """
    _configuration = configuration.Configuration()

    def setUp(self):
        used_api_client = api_client.ApiClient(configuration=self._configuration)
        self.api = post.ApiForpost(api_client=used_api_client)  # noqa: E501

    def tearDown(self):
        pass

    response_status = 200

    def test_all_string_formats_ignore_objects_passes(self):
        # all string formats ignore objects
        accept_content_type = 'application/json'

        with patch.object(urllib3.PoolManager, 'request') as mock_request:
            payload = (
                {
                }
            )
            mock_request.return_value = self.response(
                self.json_bytes(payload),
                status=self.response_status
            )
            api_response = self.api.post(
                accept_content_types=(accept_content_type,)
            )
            self.assert_pool_manager_request_called_with(
                mock_request,
                self._configuration.host + '/responseBody/postIpv6FormatResponseBodyForContentTypes',
                method='post'.upper(),
                accept_content_type=accept_content_type,
            )

            assert isinstance(api_response.response, urllib3.HTTPResponse)
            assert isinstance(api_response.body, post.SchemaFor200ResponseBodyApplicationJson)
            deserialized_response_body = post.SchemaFor200ResponseBodyApplicationJson._from_openapi_data(
                payload,
                _configuration=self._configuration
            )
            assert api_response.body == deserialized_response_body

    def test_all_string_formats_ignore_booleans_passes(self):
        # all string formats ignore booleans
        accept_content_type = 'application/json'

        with patch.object(urllib3.PoolManager, 'request') as mock_request:
            payload = (
                False
            )
            mock_request.return_value = self.response(
                self.json_bytes(payload),
                status=self.response_status
            )
            api_response = self.api.post(
                accept_content_types=(accept_content_type,)
            )
            self.assert_pool_manager_request_called_with(
                mock_request,
                self._configuration.host + '/responseBody/postIpv6FormatResponseBodyForContentTypes',
                method='post'.upper(),
                accept_content_type=accept_content_type,
            )

            assert isinstance(api_response.response, urllib3.HTTPResponse)
            assert isinstance(api_response.body, post.SchemaFor200ResponseBodyApplicationJson)
            deserialized_response_body = post.SchemaFor200ResponseBodyApplicationJson._from_openapi_data(
                payload,
                _configuration=self._configuration
            )
            assert api_response.body == deserialized_response_body

    def test_all_string_formats_ignore_integers_passes(self):
        # all string formats ignore integers
        accept_content_type = 'application/json'

        with patch.object(urllib3.PoolManager, 'request') as mock_request:
            payload = (
                12
            )
            mock_request.return_value = self.response(
                self.json_bytes(payload),
                status=self.response_status
            )
            api_response = self.api.post(
                accept_content_types=(accept_content_type,)
            )
            self.assert_pool_manager_request_called_with(
                mock_request,
                self._configuration.host + '/responseBody/postIpv6FormatResponseBodyForContentTypes',
                method='post'.upper(),
                accept_content_type=accept_content_type,
            )

            assert isinstance(api_response.response, urllib3.HTTPResponse)
            assert isinstance(api_response.body, post.SchemaFor200ResponseBodyApplicationJson)
            deserialized_response_body = post.SchemaFor200ResponseBodyApplicationJson._from_openapi_data(
                payload,
                _configuration=self._configuration
            )
            assert api_response.body == deserialized_response_body

    def test_all_string_formats_ignore_floats_passes(self):
        # all string formats ignore floats
        accept_content_type = 'application/json'

        with patch.object(urllib3.PoolManager, 'request') as mock_request:
            payload = (
                13.7
            )
            mock_request.return_value = self.response(
                self.json_bytes(payload),
                status=self.response_status
            )
            api_response = self.api.post(
                accept_content_types=(accept_content_type,)
            )
            self.assert_pool_manager_request_called_with(
                mock_request,
                self._configuration.host + '/responseBody/postIpv6FormatResponseBodyForContentTypes',
                method='post'.upper(),
                accept_content_type=accept_content_type,
            )

            assert isinstance(api_response.response, urllib3.HTTPResponse)
            assert isinstance(api_response.body, post.SchemaFor200ResponseBodyApplicationJson)
            deserialized_response_body = post.SchemaFor200ResponseBodyApplicationJson._from_openapi_data(
                payload,
                _configuration=self._configuration
            )
            assert api_response.body == deserialized_response_body

    def test_all_string_formats_ignore_arrays_passes(self):
        # all string formats ignore arrays
        accept_content_type = 'application/json'

        with patch.object(urllib3.PoolManager, 'request') as mock_request:
            payload = (
                [
                ]
            )
            mock_request.return_value = self.response(
                self.json_bytes(payload),
                status=self.response_status
            )
            api_response = self.api.post(
                accept_content_types=(accept_content_type,)
            )
            self.assert_pool_manager_request_called_with(
                mock_request,
                self._configuration.host + '/responseBody/postIpv6FormatResponseBodyForContentTypes',
                method='post'.upper(),
                accept_content_type=accept_content_type,
            )

            assert isinstance(api_response.response, urllib3.HTTPResponse)
            assert isinstance(api_response.body, post.SchemaFor200ResponseBodyApplicationJson)
            deserialized_response_body = post.SchemaFor200ResponseBodyApplicationJson._from_openapi_data(
                payload,
                _configuration=self._configuration
            )
            assert api_response.body == deserialized_response_body

    def test_all_string_formats_ignore_nulls_passes(self):
        # all string formats ignore nulls
        accept_content_type = 'application/json'

        with patch.object(urllib3.PoolManager, 'request') as mock_request:
            payload = (
                None
            )
            mock_request.return_value = self.response(
                self.json_bytes(payload),
                status=self.response_status
            )
            api_response = self.api.post(
                accept_content_types=(accept_content_type,)
            )
            self.assert_pool_manager_request_called_with(
                mock_request,
                self._configuration.host + '/responseBody/postIpv6FormatResponseBodyForContentTypes',
                method='post'.upper(),
                accept_content_type=accept_content_type,
            )

            assert isinstance(api_response.response, urllib3.HTTPResponse)
            assert isinstance(api_response.body, post.SchemaFor200ResponseBodyApplicationJson)
            deserialized_response_body = post.SchemaFor200ResponseBodyApplicationJson._from_openapi_data(
                payload,
                _configuration=self._configuration
            )
            assert api_response.body == deserialized_response_body




if __name__ == '__main__':
    unittest.main()