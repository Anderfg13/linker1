variable "region" {
  description = "AWS region. Learner Lab only allows us-east-1 and us-west-2."
  type        = string
  default     = "us-east-1"
}

variable "enable_cloudfront" {
  description = "Put a CloudFront distribution in front of the ALB to get HTTPS on a *.cloudfront.net URL without owning a domain."
  type        = bool
  default     = true
}
